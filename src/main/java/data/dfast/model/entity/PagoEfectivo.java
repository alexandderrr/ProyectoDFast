package data.dfast.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("EFECTIVO")
public class PagoEfectivo extends Pago {
    @Column(name = "efectivo_recibido", precision = 10, scale = 2)
    private BigDecimal efectivoRecibido;

    public BigDecimal getEfectivoRecibido() { return efectivoRecibido; }
    public void setEfectivoRecibido(BigDecimal efectivoRecibido) { this.efectivoRecibido = efectivoRecibido; }
    public BigDecimal getCambio() {
        return efectivoRecibido == null || getMonto() == null
                ? BigDecimal.ZERO : efectivoRecibido.subtract(getMonto());
    }
}