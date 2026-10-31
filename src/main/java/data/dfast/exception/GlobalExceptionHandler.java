package data.dfast.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Traduce las excepciones de negocio a respuestas HTTP (texto plano, como espera la web). */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EstadoPedidoInvalidoException.class)
    public ResponseEntity<String> estadoInvalido(EstadoPedidoInvalidoException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<String> noEncontrado(RecursoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({ReglaNegocioException.class, IllegalArgumentException.class})
    public ResponseEntity<String> reglaNegocio(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
