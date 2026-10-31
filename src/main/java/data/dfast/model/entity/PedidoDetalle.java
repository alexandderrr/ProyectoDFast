package data.dfast.model.entity;

import jakarta.persistence.*;

/** Una línea del pedido: qué producto, cuántos y a qué precio se vendió. */
@Entity
@Table(name = "pedido_detalles")
public class PedidoDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    protected PedidoDetalle() {
    }

    public PedidoDetalle(Pedido pedido, Producto producto, int cantidad) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
    }

    public double getSubtotal() {
        return precioUnitario * cantidad;
    }

    public Long getId() { return id; }
    public Pedido getPedido() { return pedido; }
    public Producto getProducto() { return producto; }
    public Integer getCantidad() { return cantidad; }
    public Double getPrecioUnitario() { return precioUnitario; }
}
