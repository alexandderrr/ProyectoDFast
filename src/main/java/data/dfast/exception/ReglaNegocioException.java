package data.dfast.exception;

/** Excepción base para cualquier regla de negocio que se incumple. */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
