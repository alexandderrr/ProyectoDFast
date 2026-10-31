package data.dfast.model.pago;

import data.dfast.exception.PagoInvalidoException;

/**
 * Plantilla de pago: procesar() siempre valida y luego cobra,
 * pero cada método de pago define cómo hace ambas cosas.
 */
public abstract class Pago {

    private final double monto;

    protected Pago(double monto) {
        if (monto <= 0) {
            throw new PagoInvalidoException("El monto a pagar debe ser mayor que cero.");
        }
        this.monto = monto;
    }

    public double getMonto() { return monto; }

    public abstract String getMetodo();

    /** Si el cobro se hace al registrar el pedido (true) o al entregarlo (false). */
    public boolean cobraAlCrearPedido() {
        return true;
    }

    protected abstract void validar();

    protected abstract ResultadoPago ejecutarCobro();

    public final ResultadoPago procesar() {
        validar();
        return ejecutarCobro();
    }

    protected String generarReferencia(String prefijo) {
        return prefijo + "-" + Long.toString(System.nanoTime(), 36).toUpperCase();
    }
}
