package data.dfast.exception;

/** Se lanza cuando los datos de un pago no son válidos. */
public class PagoInvalidoException extends ReglaNegocioException {

    public PagoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
