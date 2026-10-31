package data.dfast.model.pago;

import data.dfast.exception.PagoInvalidoException;

/** Pago por transferencia: se verifica la referencia y que el monto coincida. */
public class PagoTransferencia extends Pago {

    private final String referencia;
    private final double montoTransferido;

    public PagoTransferencia(double monto, String referencia, double montoTransferido) {
        super(monto);
        this.referencia = referencia;
        this.montoTransferido = montoTransferido;
    }

    @Override public String getMetodo() { return "TRANSFERENCIA"; }

    @Override
    protected void validar() {
        if (referencia == null || !referencia.matches("[A-Za-z0-9]{6,}")) {
            throw new PagoInvalidoException(
                    "La referencia debe tener al menos 6 caracteres alfanuméricos.");
        }
        if (Math.abs(montoTransferido - getMonto()) > 0.01) {
            throw new PagoInvalidoException("El monto transferido no coincide con el total.");
        }
    }

    @Override
    protected ResultadoPago ejecutarCobro() {
        return new ResultadoPago(getMetodo(), getMonto(), referencia.toUpperCase(),
                "Transferencia verificada.");
    }
}
