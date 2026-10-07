/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.dfast.model.entity;

/**
 *
 * @author Brandon
 */
import jakarta.persistence.*;

@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String descripcion;
    
    @Column(nullable = false)
    private Double peso;
    
    @Column(name = "direccion_recogida", nullable = false)
    private String direccionRecogida;
    
    @Column(name = "direccion_entrega", nullable = false)
    private String direccionEntrega;
    
    @Column(nullable = false)
    private String estado = "PENDIENTE"; // Valores: PENDIENTE, EN_RUTA, ENTREGADO

    // Relación: Un cliente (Usuario) puede tener muchos pedidos
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    // Relación: Un repartidor (Usuario) puede entregar muchos pedidos
    // Es nullable porque al crear un pedido nuevo, aún no tiene repartidor asignado.
    @ManyToOne
    @JoinColumn(name = "repartidor_id")
    private Usuario repartidor;

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public String getDireccionRecogida() { return direccionRecogida; }
    public void setDireccionRecogida(String direccionRecogida) { this.direccionRecogida = direccionRecogida; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }

    public Usuario getRepartidor() { return repartidor; }
    public void setRepartidor(Usuario repartidor) { this.repartidor = repartidor; }
}