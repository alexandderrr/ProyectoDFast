package data.dfast.model.enums;

import data.dfast.exception.EstadoPedidoInvalidoException;
import data.dfast.exception.ReglaNegocioException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Ciclo de vida de un pedido. Cada estado conoce a qué estados puede pasar,
 * así que no hace falta ningún if/else para validar una transición.
 */
public enum EstadoPedido {

    CREADO("Pedido recibido, esperando confirmación del comercio"),
    CONFIRMADO("El comercio aceptó el pedido"),
    EN_PREPARACION("El comercio está preparando tu pedido"),
    LISTO("Pedido listo, esperando al repartidor"),
    EN_CAMINO("El repartidor va en camino a tu dirección"),
    ENTREGADO("Pedido entregado"),
    CANCELADO("Pedido cancelado por el cliente"),
    RECHAZADO("Pedido rechazado por el comercio");

    private static final Map<EstadoPedido, Set<EstadoPedido>> TRANSICIONES =
            new EnumMap<>(EstadoPedido.class);

    static {
        TRANSICIONES.put(CREADO, EnumSet.of(CONFIRMADO, RECHAZADO, CANCELADO));
        TRANSICIONES.put(CONFIRMADO, EnumSet.of(EN_PREPARACION, CANCELADO));
        TRANSICIONES.put(EN_PREPARACION, EnumSet.of(LISTO));
        TRANSICIONES.put(LISTO, EnumSet.of(EN_CAMINO));
        TRANSICIONES.put(EN_CAMINO, EnumSet.of(ENTREGADO));
        TRANSICIONES.put(ENTREGADO, EnumSet.noneOf(EstadoPedido.class));
        TRANSICIONES.put(CANCELADO, EnumSet.noneOf(EstadoPedido.class));
        TRANSICIONES.put(RECHAZADO, EnumSet.noneOf(EstadoPedido.class));
    }

    private final String descripcionSeguimiento;

    EstadoPedido(String descripcionSeguimiento) {
        this.descripcionSeguimiento = descripcionSeguimiento;
    }

    public String getDescripcionSeguimiento() {
        return descripcionSeguimiento;
    }

    public boolean puedePasarA(EstadoPedido destino) {
        return TRANSICIONES.get(this).contains(destino);
    }

    /** Lanza EstadoPedidoInvalidoException si la transición no está permitida. */
    public void validarTransicion(EstadoPedido destino) {
        if (!puedePasarA(destino)) {
            throw new EstadoPedidoInvalidoException(this, destino);
        }
    }

    public boolean esFinal() {
        return TRANSICIONES.get(this).isEmpty();
    }

    public static EstadoPedido desdeTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new ReglaNegocioException("Debes indicar el estado.");
        }
        try {
            return valueOf(texto.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException("Estado desconocido: " + texto);
        }
    }
}
