package data.dfast.service;

import data.dfast.dto.ComercioRespuestaDTO;
import data.dfast.dto.ProductoRequestDTO;
import data.dfast.dto.ProductoRespuestaDTO;
import data.dfast.exception.RecursoNoEncontradoException;
import data.dfast.exception.ReglaNegocioException;
import data.dfast.model.entity.Producto;
import data.dfast.model.entity.Usuario;
import data.dfast.repository.CalificacionRepository;
import data.dfast.repository.ProductoRepository;
import data.dfast.repository.UsuarioRepository;
import data.dfast.util.PedidoMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComercioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CalificacionRepository calificacionRepository;

    public List<ComercioRespuestaDTO> listarComercios() {
        return usuarioRepository.findByRolIgnoreCase("COMERCIO").stream()
                .map(comercio -> {
                    Double promedio = calificacionRepository.promedioComercio(comercio.getId());
                    long total = calificacionRepository.countByComercioId(comercio.getId());
                    return new ComercioRespuestaDTO(
                            comercio.getId(),
                            comercio.getNombre(),
                            PedidoMapper.nombreComercial(comercio),
                            promedio == null ? 0 : Math.round(promedio * 10) / 10.0,
                            total);
                })
                .toList();
    }

    public List<ProductoRespuestaDTO> listarProductos(Long comercioId, boolean soloDisponibles) {
        buscarComercio(comercioId);
        return productoRepository.findByComercioId(comercioId).stream()
                .filter(p -> !soloDisponibles || p.isDisponible())
                .map(ComercioService::aDTO)
                .toList();
    }

    @Transactional
    public ProductoRespuestaDTO crearProducto(Long comercioId, ProductoRequestDTO request) {
        Usuario comercio = validarDueno(comercioId, request.usuarioId());

        if (request.nombre() == null || request.nombre().isBlank()) {
            throw new ReglaNegocioException("El producto necesita un nombre.");
        }
        if (request.precio() == null || request.precio() <= 0) {
            throw new ReglaNegocioException("El precio debe ser mayor que cero.");
        }
        double peso = request.pesoKg() == null ? 0.5 : request.pesoKg();
        if (peso <= 0) {
            throw new ReglaNegocioException("El peso debe ser mayor que cero.");
        }

        Producto producto = new Producto();
        producto.setComercio(comercio);
        producto.setNombre(request.nombre().trim());
        producto.setPrecio(request.precio());
        producto.setPesoKg(peso);
        producto.setDisponible(true);

        return aDTO(productoRepository.save(producto));
    }

    @Transactional
    public ProductoRespuestaDTO cambiarDisponibilidad(
            Long comercioId, Long productoId, Long usuarioId, boolean disponible) {
        validarDueno(comercioId, usuarioId);

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto no existe."));

        if (!producto.getComercio().getId().equals(comercioId)) {
            throw new ReglaNegocioException("El producto no pertenece a este comercio.");
        }
        producto.setDisponible(disponible);
        return aDTO(productoRepository.save(producto));
    }

    private Usuario buscarComercio(Long comercioId) {
        Usuario comercio = usuarioRepository.findById(comercioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El comercio no existe."));
        if (!"COMERCIO".equalsIgnoreCase(comercio.getRol())) {
            throw new ReglaNegocioException("El usuario indicado no tiene el rol COMERCIO.");
        }
        return comercio;
    }

    // Solo el propio comercio (o un admin) puede tocar sus productos
    private Usuario validarDueno(Long comercioId, Long usuarioId) {
        Usuario comercio = buscarComercio(comercioId);
        if (usuarioId == null) {
            throw new ReglaNegocioException("Falta indicar quién realiza la acción.");
        }
        Usuario actor = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no existe."));

        boolean esDueno = actor.getId().equals(comercioId);
        boolean esAdmin = "ADMIN".equalsIgnoreCase(actor.getRol());
        if (!esDueno && !esAdmin) {
            throw new ReglaNegocioException("Solo el comercio puede administrar sus productos.");
        }
        return comercio;
    }

    private static ProductoRespuestaDTO aDTO(Producto p) {
        return new ProductoRespuestaDTO(
                p.getId(), p.getNombre(), p.getPrecio(), p.getPesoKg(), p.isDisponible());
    }
}
