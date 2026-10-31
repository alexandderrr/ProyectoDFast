package data.dfast.model.pago;

import data.dfast.exception.PagoInvalidoException;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;

/** Crea el Pago correcto según el método elegido, sin cadenas de if/else. */
public final class PagoFactory {

    private static final Map<String, BiFunction<Double, DatosPago, Pago>> CREADORES = Map.of(
            "EFECTIVO", (monto, d) -> new PagoEfectivo(monto, monto),
            "TARJETA", (monto, d) -> new PagoTarjeta(monto, d.numeroTarjeta(),
                    d.titularTarjeta(), d.vencimientoTarjeta(), d.cvv()),
            "TRANSFERENCIA", (monto, d) -> new PagoTransferencia(monto,
                    d.referenciaTransferencia(), monto));

    private PagoFactory() {
    }

    public static Pago crear(String metodo, double monto, DatosPago datos) {
        String clave = metodo == null ? "" : metodo.trim().toUpperCase(Locale.ROOT);
        BiFunction<Double, DatosPago, Pago> creador = CREADORES.get(clave);
        if (creador == null) {
            throw new PagoInvalidoException("Método de pago no válido: " + metodo);
        }
        return creador.apply(monto, datos == null ? DatosPago.vacio() : datos);
    }
}
