package data.dfast.service;

import data.dfast.dto.CalificacionRequestDTO;
import data.dfast.model.entity.Calificacion;
import data.dfast.model.entity.Pedido;
import data.dfast.model.entity.Usuario;
import data.dfast.repository.CalificacionRepository;
import data.dfast.repository.PedidoRepository;
import data.dfast.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CalificacionService {
    private final CalificacionRepository calificacionRepository;
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    public CalificacionService(CalificacionRepository calificacionRepository,
                               PedidoRepository pedidoRepository,
                               UsuarioRepository usuarioRepository) {
        this.calificacionRepository = calificacionRepository;
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Calificacion crear(CalificacionRequestDTO request) {
        if (request.getPedidoId() == null || request.getUsuarioId() == null)
            throw new IllegalArgumentException("Debe indicar el pedido y el usuario.");
        if (request.getPuntuacion() == null || request.getPuntuacion() < 1 || request.getPuntuacion() > 5)
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5.");
        Pedido pedido = pedidoRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new IllegalArgumentException("No existe el pedido indicado."));
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("No existe el usuario indicado."));
        if (calificacionRepository.existsByPedidoIdAndUsuarioId(pedido.getId(), usuario.getId()))
            throw new IllegalArgumentException("Este usuario ya calificó el pedido.");
        Calificacion calificacion = new Calificacion();
        calificacion.setPedido(pedido);
        calificacion.setUsuario(usuario);
        calificacion.setPuntuacion(request.getPuntuacion());
        calificacion.setComentario(request.getComentario() == null ? null : request.getComentario().trim());
        return calificacionRepository.save(calificacion);
    }

    public List<Calificacion> listarPorPedido(Long pedidoId) {
        return calificacionRepository.findByPedidoId(pedidoId);
    }
}