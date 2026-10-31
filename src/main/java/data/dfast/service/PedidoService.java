package data.dfast.service;

import data.dfast.dto.ItemPedidoDTO;
import data.dfast.dto.PedidoRequestDTO;
import data.dfast.dto.PedidoRespuestaDTO;
import data.dfast.exception.RecursoNoEncontradoException;
import data.dfast.exception.ReglaNegocioException;
import data.dfast.model.entity.CodigoPromocion;
import data.dfast.model.entity.Pedido;
import data.dfast.model.entity.PedidoDetalle;
import data.dfast.model.entity.Producto;
import data.dfast.model.entity.Usuario;
import data.dfast.model.enums.EstadoPago;
import data.dfast.model.enums.EstadoPedido;
import data.dfast.model.pago.DatosPago;
import data.dfast.model.pago.Pago;
import data.dfast.model.pago.PagoFactory;
import data.dfast.model.pago.ResultadoPago;
import data.dfast.model.promocion.Promocion;
import data.dfast.model.promocion.PromocionFactory;
import data.dfast.repository.CodigoPromocionRepository;
import data.dfast.repository.PedidoRepository;
import data.dfast.repository.ProductoRepository;
import data.dfast.repository.UsuarioRepository;
import data.dfast.util.PedidoMapper;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    // A qué estados puede mover un pedido cada rol
    private static final Map<String, Set<EstadoPedido>> PERMISOS_POR_ROL = Map.of(
            "COMERCIO", EnumSet.of(EstadoPedido.CONFIRMADO, EstadoPedido.RECHAZADO,
                    EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO),
            "CLIENTE", EnumSet.of(EstadoPedido.CANCELADO),
            "REPARTIDOR", EnumSet.of(EstadoPedido.EN_CAMINO, EstadoPedido.ENTREGADO),
            "ADMIN", EnumSet.allOf(EstadoPedido.class));

    // Estados en los que todavía tiene sentido asignar (o reasignar) repartidor
    private static final Set<EstadoPedido> ESTADOS_ASIGNABLES = EnumSet.of(
            EstadoPedido.CONFIRMADO, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO);

    // Costo de envío base mientras no se calcule por distancia (en quetzales)
    private static final double COSTO_ENVIO_BASE = 15.0;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CodigoPromocionRepository codigoPromocionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> buscarCliente(Long clienteId) {
        return usuarioRepository.findById(clienteId);
    }

    @Transactional
    public Pedido crearPedido(PedidoRequestDTO request, Usuario cliente) {

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setDireccionRecogida(request.getDireccionRecogida());
        nuevoPedido.setDireccionEntrega(request.getDireccionEntrega());
        nuevoPedido.setLatitudRecogida(request.getLatitudRecogida());
        nuevoPedido.setLongitudRecogida(request.getLongitudRecogida());
        nuevoPedido.setLatitudEntrega(request.getLatitudEntrega());
        nuevoPedido.setLongitudEntrega(request.getLongitudEntrega());
        nuevoPedido.setCliente(cliente);

        Usuario comercio = null;
        if (request.getComercioId() != null) {
            comercio = usuarioRepository.findById(request.getComercioId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                    "El comercio indicado no existe."));

            if (!"COMERCIO".equalsIgnoreCase(comercio.getRol())) {
                throw new ReglaNegocioException(
                        "El usuario indicado no tiene el rol COMERCIO.");
            }
            nuevoPedido.setComercio(comercio);
        }

        // 1. Productos (o descripción y peso manuales si no hay productos)
        double subtotal = 0;
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            if (comercio == null) {
                throw new ReglaNegocioException("Selecciona un comercio para pedir productos.");
            }
            double peso = 0;
            List<String> partes = new ArrayList<>();

            for (ItemPedidoDTO item : request.getItems()) {
                if (item.getCantidad() == null || item.getCantidad() <= 0) {
                    throw new ReglaNegocioException("Las cantidades deben ser mayores que cero.");
                }
                Producto producto = productoRepository.findById(
                        item.getProductoId() == null ? -1L : item.getProductoId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Uno de los productos ya no existe."));

                if (!producto.getComercio().getId().equals(comercio.getId())) {
                    throw new ReglaNegocioException(
                            "El producto " + producto.getNombre() + " no es de este comercio.");
                }
                if (!producto.isDisponible()) {
                    throw new ReglaNegocioException(
                            "El producto " + producto.getNombre() + " no está disponible.");
                }

                PedidoDetalle detalle = new PedidoDetalle(nuevoPedido, producto, item.getCantidad());
                nuevoPedido.getDetalles().add(detalle);
                subtotal += detalle.getSubtotal();
                peso += producto.getPesoKg() * item.getCantidad();
                partes.add(item.getCantidad() + "x " + producto.getNombre());
            }
            nuevoPedido.setDescripcion(String.join(", ", partes));
            nuevoPedido.setPeso(redondear(peso));
        } else {
            nuevoPedido.setDescripcion(request.getDescripcion());
            nuevoPedido.setPeso(request.getPeso());
        }

        if (nuevoPedido.getDescripcion() == null || nuevoPedido.getDescripcion().isBlank()
                || nuevoPedido.getPeso() == null || nuevoPedido.getPeso() <= 0) {
            throw new ReglaNegocioException(
                    "Selecciona productos o indica la descripción y el peso del pedido.");
        }
        if (nuevoPedido.getDireccionEntrega() == null
                || nuevoPedido.getDireccionEntrega().isBlank()) {
            throw new ReglaNegocioException("Indica la dirección de entrega.");
        }
        if (nuevoPedido.getDireccionRecogida() == null
                || nuevoPedido.getDireccionRecogida().isBlank()) {
            nuevoPedido.setDireccionRecogida(
                    "Comercio: " + (comercio == null ? "sin indicar" : PedidoMapper.nombreComercial(comercio)));
        }

        // 2. Promoción (polimorfismo: cada tipo calcula su descuento)
        double envio = COSTO_ENVIO_BASE;
        double total = subtotal + envio;
        double descuento = 0;

        String codigo = request.getCodigoPromocion();
        if (codigo != null && !codigo.isBlank()) {
            CodigoPromocion guardada = codigoPromocionRepository
                    .findByCodigoIgnoreCase(codigo.trim())
                    .filter(CodigoPromocion::isActiva)
                    .orElseThrow(() -> new ReglaNegocioException(
                    "El código de promoción no existe o ya no está activo."));

            Promocion promocion = PromocionFactory.crear(
                    guardada.getCodigo(), guardada.getTipo(), guardada.getValor());
            descuento = promocion.calcularDescuento(subtotal, envio);
            total = promocion.calcularTotal(subtotal, envio);
            nuevoPedido.setCodigoPromocion(guardada.getCodigo());
        }

        nuevoPedido.setSubtotal(redondear(subtotal));
        nuevoPedido.setCostoEnvio(redondear(envio));
        nuevoPedido.setDescuento(redondear(descuento));
        nuevoPedido.setTotal(redondear(total));

        // 3. Pago (polimorfismo: cada método valida y cobra a su manera)
        String metodo = (request.getMetodoPago() == null || request.getMetodoPago().isBlank())
                ? "EFECTIVO" : request.getMetodoPago();
        nuevoPedido.setMetodoPago(metodo.trim().toUpperCase(Locale.ROOT));

        if (nuevoPedido.getTotal() <= 0) {
            nuevoPedido.setEstadoPago(EstadoPago.PAGADO);
            nuevoPedido.setReferenciaPago("SIN-COBRO");
        } else {
            DatosPago datos = new DatosPago(
                    request.getNumeroTarjeta(), request.getTitularTarjeta(),
                    request.getVencimientoTarjeta(), request.getCvv(),
                    request.getReferenciaTransferencia());
            Pago pago = PagoFactory.crear(nuevoPedido.getMetodoPago(), nuevoPedido.getTotal(), datos);

            if (pago.cobraAlCrearPedido()) {
                ResultadoPago resultado = pago.procesar();
                nuevoPedido.setEstadoPago(EstadoPago.PAGADO);
                nuevoPedido.setReferenciaPago(resultado.referencia());
            } else {
                nuevoPedido.setEstadoPago(EstadoPago.PENDIENTE); // se cobra al entregar
            }
        }

        return pedidoRepository.save(nuevoPedido);
    }

    // ---------- Consultas ----------
    public List<PedidoRespuestaDTO> listarTodosLosPedidos() {
        return aDTOs(pedidoRepository.findAll());
    }

    public List<PedidoRespuestaDTO> listarPedidosPorCliente(Long clienteId) {
        return aDTOs(pedidoRepository.findByClienteId(clienteId));
    }

    public List<PedidoRespuestaDTO> listarPedidosPorComercio(Long comercioId) {
        Usuario comercio = usuarioRepository.findById(comercioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El comercio no existe."));

        if (!"COMERCIO".equalsIgnoreCase(comercio.getRol())) {
            throw new ReglaNegocioException("El usuario indicado no tiene el rol COMERCIO.");
        }
        return aDTOs(pedidoRepository.findByComercioId(comercioId));
    }

    // Pedidos listos que todavía no tienen repartidor
    public List<PedidoRespuestaDTO> listarPedidosDisponibles() {
        return aDTOs(pedidoRepository.findByRepartidorIsNullAndEstado(EstadoPedido.LISTO));
    }

    public List<PedidoRespuestaDTO> listarPedidosPorRepartidor(Long repartidorId) {
        return aDTOs(pedidoRepository.findByRepartidorId(repartidorId));
    }

    // ---------- Asignación ----------
    @Transactional
    public PedidoRespuestaDTO aceptarPedido(Long pedidoId, Long repartidorId) {
        Usuario repartidor = buscarRepartidor(repartidorId);

        int filas = pedidoRepository.asignarSiDisponible(
                pedidoId, repartidor, EstadoPedido.LISTO);

        if (filas == 0) {
            throw new ReglaNegocioException(
                    "Este pedido ya no está disponible. Actualiza la lista.");
        }
        return PedidoMapper.aDTO(buscarPedido(pedidoId));
    }

    public PedidoRespuestaDTO asignarRepartidor(Long pedidoId, Long repartidorId) {
        Pedido pedido = buscarPedido(pedidoId);
        Usuario repartidor = buscarRepartidor(repartidorId);

        if (!ESTADOS_ASIGNABLES.contains(pedido.getEstado())) {
            throw new ReglaNegocioException(
                    "No se puede asignar repartidor a un pedido en estado "
                    + pedido.getEstado() + ".");
        }
        pedido.setRepartidor(repartidor);
        return PedidoMapper.aDTO(pedidoRepository.save(pedido));
    }

    // ---------- Cambio de estado (comercio, cliente, repartidor o admin) ----------
    @Transactional
    public PedidoRespuestaDTO actualizarEstado(Long pedidoId, Long actorId, String nuevoEstado) {
        Usuario actor = usuarioRepository.findById(actorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no existe."));
        Pedido pedido = buscarPedido(pedidoId);
        EstadoPedido destino = EstadoPedido.desdeTexto(nuevoEstado);

        validarPermisoDelActor(actor, pedido, destino);
        pedido.getEstado().validarTransicion(destino); // lanza EstadoPedidoInvalidoException
        pedido.setEstado(destino);

        if (destino == EstadoPedido.ENTREGADO && pedido.getEstadoPago() == EstadoPago.PENDIENTE) {
            // El pago en efectivo se cobra al confirmar la entrega
            Pago pago = PagoFactory.crear(
                    pedido.getMetodoPago(), pedido.getTotal(), DatosPago.vacio());
            ResultadoPago resultado = pago.procesar();
            pedido.setEstadoPago(EstadoPago.PAGADO);
            pedido.setReferenciaPago(resultado.referencia());
        }

        return PedidoMapper.aDTO(pedidoRepository.save(pedido));
    }

    // Se mantiene por compatibilidad con el endpoint PATCH que ya usaba la web
    public PedidoRespuestaDTO actualizarEstadoPedido(
            Long pedidoId, Long repartidorId, String nuevoEstado) {
        return actualizarEstado(pedidoId, repartidorId, nuevoEstado);
    }

    private void validarPermisoDelActor(Usuario actor, Pedido pedido, EstadoPedido destino) {
        String rol = actor.getRol() == null ? "" : actor.getRol().toUpperCase(Locale.ROOT);

        Set<EstadoPedido> permitidos = PERMISOS_POR_ROL.get(rol);
        if (permitidos == null || !permitidos.contains(destino)) {
            throw new ReglaNegocioException(
                    "El rol " + rol + " no puede cambiar un pedido a " + destino + ".");
        }
        if ("ADMIN".equals(rol)) {
            return;
        }

        Usuario responsable = switch (rol) {
            case "COMERCIO" -> pedido.getComercio();
            case "CLIENTE" -> pedido.getCliente();
            default -> pedido.getRepartidor();
        };
        if (responsable == null || !responsable.getId().equals(actor.getId())) {
            throw new ReglaNegocioException("Este pedido no está asociado a tu usuario.");
        }
    }

    // ---------- Auxiliares ----------
    public Pedido buscarPedidoPorId(Long id) {
        return buscarPedido(id);
    }

    private Pedido buscarPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El pedido no existe."));
    }

    private Usuario buscarRepartidor(Long id) {
        Usuario repartidor = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El repartidor no existe."));
        if (!"REPARTIDOR".equalsIgnoreCase(repartidor.getRol())) {
            throw new ReglaNegocioException("El usuario indicado no tiene el rol REPARTIDOR.");
        }
        return repartidor;
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private List<PedidoRespuestaDTO> aDTOs(List<Pedido> pedidos) {
        return pedidos.stream().map(PedidoMapper::aDTO).collect(Collectors.toList());
    }
}
