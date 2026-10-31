package data.dfast.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calificaciones")
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Un pedido solo se califica una vez
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "comercio_id")
    private Usuario comercio;

    @ManyToOne
    @JoinColumn(name = "repartidor_id")
    private Usuario repartidor;

    @Column(name = "puntos_comercio")
    private Integer puntosComercio;

    @Column(name = "puntos_repartidor")
    private Integer puntosRepartidor;

    private String comentario;

    private LocalDateTime fecha = LocalDateTime.now();

    public Long getId() { return id; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }
    public Usuario getComercio() { return comercio; }
    public void setComercio(Usuario comercio) { this.comercio = comercio; }
    public Usuario getRepartidor() { return repartidor; }
    public void setRepartidor(Usuario repartidor) { this.repartidor = repartidor; }
    public Integer getPuntosComercio() { return puntosComercio; }
    public void setPuntosComercio(Integer puntosComercio) { this.puntosComercio = puntosComercio; }
    public Integer getPuntosRepartidor() { return puntosRepartidor; }
    public void setPuntosRepartidor(Integer puntosRepartidor) { this.puntosRepartidor = puntosRepartidor; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getFecha() { return fecha; }
}
