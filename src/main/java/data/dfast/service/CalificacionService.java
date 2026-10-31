package data.dfast.service;

import data.dfast.dto.CalificacionRequestDTO;
import data.dfast.dto.CalificacionRespuestaDTO;
import data.dfast.dto.PedidoRespuestaDTO;
import data.dfast.exception.RecursoNoEncontradoException;
import data.dfast.exception.ReglaNegocioException;
import data.dfast.model.entity.Calificacion;
import data.dfast.model.entity.Pedido;
import data.dfast.model.entity.Usuario;
import data.dfast.model.enums.EstadoPedido;
import data.dfast.model.repartidor.Repartidor;
import data.dfast.model.repartidor.RepartidorFactory;
import data.dfast.repository.CalificacionRepository;
import data.dfast.repository.PedidoRepository;
import data.dfast.util.PedidoMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public CalificacionRespuestaDTO registrar(CalificacionRequestDTO request) {
        Pedido pedido = pedidoRepository.findById(
                request.pedidoId() == null ? -1L : request.pedidoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El pedido no existe."));

        if (request.clienteId() == null
                || !pedido.getCliente().getId().equals(request.clienteId())) {
            throw new ReglaNegocioException("Solo el cliente del pedido puede calificarlo.");
        }
        if (pedido.getEstado() != EstadoPedido.ENTREGADO) {
            throw new ReglaNegocioException("Solo puedes calificar pedidos ya entregados.");
        }
        if (calificacionRepository.existsByPedidoId(pedido.getId())) {
            throw new ReglaNegocioException("Este pedido ya fue calificado.");
        }

        Calificacion calificacion = new Calificacion();
        calificacion.setPedido(pedido);
        calificacion.setCliente(pedido.getCliente());
        calificacion.setComentario(request.comentario());

        // Comercio
        if (pedido.getComercio() != null) {
            validarPuntos(request.puntosComercio(), "del comercio");
            calificacion.setComercio(pedido.getComercio());
            calificacion.setPuntosComercio(request.puntosComercio());
        }

        // Repartidor: se usa el objeto del dominio (Calificable + Notificable)
        Usuario usuarioRepartidor = pedido.getRepartidor();
        if (usuarioRepartidor != null) {
            validarPuntos(request.puntosRepartidor(), "del repartidor");

            Repartidor repartidor = RepartidorFactory.crear(
                    usuarioRepartidor.getTipoVehiculo(), usuarioRepartidor.getId(),
                    usuarioRepartidor.getNombre(), usuarioRepartidor.getZona(),
                    usuarioRepartidor.isDisponible());

            Double promedioActual = calificacionRepository.promedioRepartidor(repartidor.getId());
            long total = calificacionRepository.countByRepartidorId(repartidor.getId());
            repartidor.cargarHistorial(promedioActual == null ? 0 : promedioActual, (int) total);

            repartidor.registrarCalificacion(request.puntosRepartidor());

            calificacion.setRepartidor(usuarioRepartidor);
            calificacion.setPuntosRepartidor(request.puntosRepartidor());

            System.out.println(repartidor.generarNotificacion(
                    "Recibiste " + request.puntosRepartidor() + " estrellas. Tu promedio ahora es "
                    + String.format("%.1f", repartidor.getPromedioCalificacion())));
        }

        return aDTO(calificacionRepository.save(calificacion));
    }

    // Pedidos entregados del cliente que todavía no ha calificado
    public List<PedidoRespuestaDTO> listarPendientes(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .filter(p -> p.getEstado() == EstadoPedido.ENTREGADO)
                .filter(p -> !calificacionRepository.existsByPedidoId(p.getId()))
                .map(PedidoMapper::aDTO)
                .toList();
    }

    public List<CalificacionRespuestaDTO> listarPorCliente(Long clienteId) {
        return calificacionRepository.findByClienteId(clienteId).stream()
                .map(CalificacionService::aDTO)
                .toList();
    }

    private void validarPuntos(Integer puntos, String quien) {
        if (puntos == null || puntos < 1 || puntos > 5) {
            throw new ReglaNegocioException(
                    "La calificación " + quien + " debe estar entre 1 y 5.");
        }
    }

    private static CalificacionRespuestaDTO aDTO(Calificacion c) {
        return new CalificacionRespuestaDTO(
                c.getId(), c.getPedido().getId(), c.getPedido().getDescripcion(),
                c.getPuntosComercio(), c.getPuntosRepartidor(), c.getComentario());
    }
}
