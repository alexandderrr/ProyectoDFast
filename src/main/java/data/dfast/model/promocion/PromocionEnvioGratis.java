package data.dfast.model.promocion;

public class PromocionEnvioGratis extends Promocion {

    public PromocionEnvioGratis(String codigo) {
        super(codigo);
    }

    @Override
    public String getDescripcion() {
        return "Envío gratis";
    }

    @Override
    public double calcularDescuento(double subtotal, double costoEnvio) {
        return costoEnvio;
    }
}
