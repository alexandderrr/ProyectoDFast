package data.dfast.dto;

import java.math.BigDecimal;

public class PromocionResultadoDTO {
    private String codigo;
    private BigDecimal descuento;
    private BigDecimal subtotal;
    private BigDecimal costoEnvio;
    private BigDecimal total;

    public PromocionResultadoDTO(String codigo, BigDecimal descuento, BigDecimal subtotal,
                                 BigDecimal costoEnvio, BigDecimal total) {
        this.codigo = codigo;
        this.descuento = descuento;
        this.subtotal = subtotal;
        this.costoEnvio = costoEnvio;
        this.total = total;
    }
    public String getCodigo() { return codigo; }
    public BigDecimal getDescuento() { return descuento; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getCostoEnvio() { return costoEnvio; }
    public BigDecimal getTotal() { return total; }
}