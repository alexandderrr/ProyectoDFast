package data.dfast.dto;

import data.dfast.model.entity.Calificacion;
import java.time.LocalDateTime;

public class CalificacionResponseDTO {
    private Long id;
    private Long pedidoId;
    private Long usuarioId;
    private Integer puntuacion;
    private String comentario;
    private LocalDateTime fechaCreacion;

    public CalificacionResponseDTO(Calificacion c) {
        id = c.getId();
        pedidoId = c.getPedido().getId();
        usuarioId = c.getUsuario().getId();
        puntuacion = c.getPuntuacion();
        comentario = c.getComentario();
        fechaCreacion = c.getFechaCreacion();
    }
    public Long getId() { return id; }
    public Long getPedidoId() { return pedidoId; }
    public Long getUsuarioId() { return usuarioId; }
    public Integer getPuntuacion() { return puntuacion; }
    public String getComentario() { return comentario; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}