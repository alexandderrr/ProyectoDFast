package data.dfast.util;

import data.dfast.dto.PedidoRespuestaDTO;
import data.dfast.model.entity.Pedido;
import data.dfast.model.entity.Usuario;

public final class PedidoMapper {

    private PedidoMapper() {
    }

    public static PedidoRespuestaDTO aDTO(Pedido pedido) {
        PedidoRespuestaDTO dto = new PedidoRespuestaDTO(
                pedido.getId(),
                pedido.getDescripcion(),
                pedido.getPeso(),
                pedido.getDireccionRecogida(),
                pedido.getDireccionEntrega(),
                pedido.getEstado().name());

        dto.setLatitudRecogida(pedido.getLatitudRecogida());
        dto.setLongitudRecogida(pedido.getLongitudRecogida());
        dto.setLatitudEntrega(pedido.getLatitudEntrega());
        dto.setLongitudEntrega(pedido.getLongitudEntrega());
        dto.setSubtotal(pedido.getSubtotal());
        dto.setCostoEnvio(pedido.getCostoEnvio());
        dto.setDescuento(pedido.getDescuento());
        dto.setTotal(pedido.getTotal());
        dto.setMetodoPago(pedido.getMetodoPago());
        dto.setEstadoPago(pedido.getEstadoPago() == null ? null : pedido.getEstadoPago().name());
        dto.setComercioNombre(nombreComercial(pedido.getComercio()));
        return dto;
    }

    public static String nombreComercial(Usuario comercio) {
        if (comercio == null) {
            return null;
        }
        String nombre = comercio.getNombreComercio();
        return (nombre == null || nombre.isBlank()) ? comercio.getNombre() : nombre;
    }
}
