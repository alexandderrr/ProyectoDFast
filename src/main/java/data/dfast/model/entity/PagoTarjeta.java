package data.dfast.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TARJETA")
public class PagoTarjeta extends Pago {
    @Column(name = "titular_tarjeta", length = 100)
    private String titularTarjeta;

    @Column(name = "ultimos_cuatro", length = 4)
    private String ultimosCuatro;

    public String getTitularTarjeta() { return titularTarjeta; }
    public void setTitularTarjeta(String titularTarjeta) { this.titularTarjeta = titularTarjeta; }
    public String getUltimosCuatro() { return ultimosCuatro; }
    public void setUltimosCuatro(String ultimosCuatro) { this.ultimosCuatro = ultimosCuatro; }
}