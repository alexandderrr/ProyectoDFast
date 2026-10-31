package data.dfast.model.promocion;

/** Clase abstracta: cada promoción calcula su descuento a su manera. */
public abstract class Promocion {

    private final String codigo;

    protected Promocion(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() { return codigo; }

    public abstract String getDescripcion();

    /** Descuento en quetzales, nunca mayor que lo que se está pagando. */
    public abstract double calcularDescuento(double subtotal, double costoEnvio);

    public double calcularTotal(double subtotal, double costoEnvio) {
        double descuento = calcularDescuento(subtotal, costoEnvio);
        return Math.max(0, subtotal + costoEnvio - descuento);
    }
}
