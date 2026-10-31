package data.dfast.dto;

public record CalificacionRespuestaDTO(Long id, Long pedidoId, String descripcionPedido,
                                       Integer puntosComercio, Integer puntosRepartidor,
                                       String comentario) {
}
