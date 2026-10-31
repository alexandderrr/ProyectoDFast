package data.dfast.dto;

public record CalificacionRequestDTO(Long pedidoId, Long clienteId, Integer puntosComercio,
                                     Integer puntosRepartidor, String comentario) {
}
