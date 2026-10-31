package data.dfast.dto;

import java.math.BigDecimal;

public class PagoRequestDTO {
    private String tipo;
    private BigDecimal monto;
    private Long pedidoId;
    private BigDecimal efectivoRecibido;
    private String titularTarjeta;
    private String ultimosCuatro;
    private String referenciaTransferencia;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public BigDecimal getEfectivoRecibido() { return efectivoRecibido; }
    public void setEfectivoRecibido(BigDecimal efectivoRecibido) { this.efectivoRecibido = efectivoRecibido; }
    public String getTitularTarjeta() { return titularTarjeta; }
    public void setTitularTarjeta(String titularTarjeta) { this.titularTarjeta = titularTarjeta; }
    public String getUltimosCuatro() { return ultimosCuatro; }
    public void setUltimosCuatro(String ultimosCuatro) { this.ultimosCuatro = ultimosCuatro; }
    public String getReferenciaTransferencia() { return referenciaTransferencia; }
    public void setReferenciaTransferencia(String referenciaTransferencia) { this.referenciaTransferencia = referenciaTransferencia; }
}