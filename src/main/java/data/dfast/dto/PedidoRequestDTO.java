package data.dfast.dto;

import java.util.List;

public class PedidoRequestDTO {

    private String descripcion;
    private Double peso;
    private String direccionRecogida;
    private String direccionEntrega;
    private Long clienteId;
    private Long comercioId;

    // Productos elegidos (si viene vacío se usan descripcion y peso manuales)
    private List<ItemPedidoDTO> items;

    // Promoción y pago
    private String codigoPromocion;
    private String metodoPago;
    private String numeroTarjeta;
    private String titularTarjeta;
    private String vencimientoTarjeta;
    private String cvv;
    private String referenciaTransferencia;

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public String getDireccionRecogida() { return direccionRecogida; }
    public void setDireccionRecogida(String direccionRecogida) { this.direccionRecogida = direccionRecogida; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getComercioId() { return comercioId; }
    public void setComercioId(Long comercioId) { this.comercioId = comercioId; }

    public List<ItemPedidoDTO> getItems() { return items; }
    public void setItems(List<ItemPedidoDTO> items) { this.items = items; }

    public String getCodigoPromocion() { return codigoPromocion; }
    public void setCodigoPromocion(String codigoPromocion) { this.codigoPromocion = codigoPromocion; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public String getTitularTarjeta() { return titularTarjeta; }
    public void setTitularTarjeta(String titularTarjeta) { this.titularTarjeta = titularTarjeta; }

    public String getVencimientoTarjeta() { return vencimientoTarjeta; }
    public void setVencimientoTarjeta(String vencimientoTarjeta) { this.vencimientoTarjeta = vencimientoTarjeta; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getReferenciaTransferencia() { return referenciaTransferencia; }
    public void setReferenciaTransferencia(String referenciaTransferencia) { this.referenciaTransferencia = referenciaTransferencia; }
}
