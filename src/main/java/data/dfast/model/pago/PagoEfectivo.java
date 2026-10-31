package data.dfast.model.pago;

import data.dfast.exception.PagoInvalidoException;

/** Pago en efectivo: se simula al confirmar la entrega. */
public class PagoEfectivo extends Pago {

    private final double montoRecibido;

    public PagoEfectivo(double monto, double montoRecibido) {
        super(monto);
        this.montoRecibido = montoRecibido;
    }

    @Override public String getMetodo() { return "EFECTIVO"; }

    @Override
    public boolean cobraAlCrearPedido() {
        return false; // el efectivo se cobra al entregar
    }

    @Override
    protected void validar() {
        if (montoRecibido < getMonto()) {
            throw new PagoInvalidoException("El efectivo recibido no cubre el total del pedido.");
        }
    }

    @Override
    protected ResultadoPago ejecutarCobro() {
        double cambio = montoRecibido - getMonto();
        return new ResultadoPago(getMetodo(), getMonto(), generarReferencia("EFE"),
                String.format("Cobrado en efectivo. Cambio: Q%.2f", cambio));
    }
}
