package data.dfast.dto;

import java.math.BigDecimal;

public class AplicarPromocionDTO {
    private String codigo;
    private BigDecimal subtotal;
    private BigDecimal costoEnvio;

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getCostoEnvio() { return costoEnvio; }
    public void setCostoEnvio(BigDecimal costoEnvio) { this.costoEnvio = costoEnvio; }
}