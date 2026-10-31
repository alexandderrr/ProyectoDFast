package data.dfast.exception;

/** Se lanza cuando un usuario, pedido, comercio, etc. no existe. */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
