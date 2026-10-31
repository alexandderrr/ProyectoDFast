package data.dfast.model.promocion;

import data.dfast.exception.ReglaNegocioException;

public class PromocionMontoFijo extends Promocion {

    private final double monto;

    public PromocionMontoFijo(String codigo, double monto) {
        super(codigo);
        if (monto <= 0) {
            throw new ReglaNegocioException("El monto de descuento debe ser mayor que cero.");
        }
        this.monto = monto;
    }

    @Override
    public String getDescripcion() {
        return "Q" + monto + " de descuento";
    }

    @Override
    public double calcularDescuento(double subtotal, double costoEnvio) {
        return Math.min(monto, subtotal);
    }
}
