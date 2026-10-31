package data.dfast.exception;

import data.dfast.model.enums.EstadoPedido;

/** Se lanza cuando se intenta una transición de estado que el ciclo de vida no permite. */
public class EstadoPedidoInvalidoException extends ReglaNegocioException {

    public EstadoPedidoInvalidoException(EstadoPedido origen, EstadoPedido destino) {
        super("Transición no permitida: " + origen + " → " + destino + ".");
    }
}
