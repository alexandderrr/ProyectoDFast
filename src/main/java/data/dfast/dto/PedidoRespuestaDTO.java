package data.dfast.dto;

public class PedidoRespuestaDTO {

    private Long id;
    private String descripcion;
    private Double peso;
    private String direccionRecogida;
    private String direccionEntrega;
    private Double latitudRecogida;
    private Double longitudRecogida;
    private Double latitudEntrega;
    private Double longitudEntrega;
    private String estado;

    // Datos de dinero y pago (pueden ser null en pedidos viejos)
    private Double subtotal;
    private Double costoEnvio;
    private Double descuento;
    private Double total;
    private String metodoPago;
    private String estadoPago;
    private String comercioNombre;

    public PedidoRespuestaDTO(
            Long id,
            String descripcion,
            Double peso,
            String direccionRecogida,
            String direccionEntrega,
            String estado) {
        this.id = id;
        this.descripcion = descripcion;
        this.peso = peso;
        this.direccionRecogida = direccionRecogida;
        this.direccionEntrega = direccionEntrega;
        this.estado = estado;
    }

    public Long getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public Double getPeso() { return peso; }
    public String getDireccionRecogida() { return direccionRecogida; }
    public String getDireccionEntrega() { return direccionEntrega; }
    public Double getLatitudRecogida() { return latitudRecogida; }
    public void setLatitudRecogida(Double latitudRecogida) { this.latitudRecogida = latitudRecogida; }
    public Double getLongitudRecogida() { return longitudRecogida; }
    public void setLongitudRecogida(Double longitudRecogida) { this.longitudRecogida = longitudRecogida; }
    public Double getLatitudEntrega() { return latitudEntrega; }
    public void setLatitudEntrega(Double latitudEntrega) { this.latitudEntrega = latitudEntrega; }
    public Double getLongitudEntrega() { return longitudEntrega; }
    public void setLongitudEntrega(Double longitudEntrega) { this.longitudEntrega = longitudEntrega; }
    public String getEstado() { return estado; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getCostoEnvio() { return costoEnvio; }
    public void setCostoEnvio(Double costoEnvio) { this.costoEnvio = costoEnvio; }

    public Double getDescuento() { return descuento; }
    public void setDescuento(Double descuento) { this.descuento = descuento; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }

    public String getComercioNombre() { return comercioNombre; }
    public void setComercioNombre(String comercioNombre) { this.comercioNombre = comercioNombre; }
}
