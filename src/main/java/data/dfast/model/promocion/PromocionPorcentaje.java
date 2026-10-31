package data.dfast.model.promocion;

import data.dfast.exception.ReglaNegocioException;

public class PromocionPorcentaje extends Promocion {

    private final double porcentaje;

    public PromocionPorcentaje(String codigo, double porcentaje) {
        super(codigo);
        if (porcentaje <= 0 || porcentaje > 100) {
            throw new ReglaNegocioException("El porcentaje debe estar entre 0 y 100.");
        }
        this.porcentaje = porcentaje;
    }

    @Override
    public String getDescripcion() {
        return porcentaje + "% de descuento en los productos";
    }

    @Override
    public double calcularDescuento(double subtotal, double costoEnvio) {
        return subtotal * porcentaje / 100;
    }
}
