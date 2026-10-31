package data.dfast.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comercio_id", nullable = false)
    private Usuario comercio;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Double precio;

    @Column(name = "peso_kg", nullable = false)
    private Double pesoKg = 0.5;

    private Boolean disponible = Boolean.TRUE;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getComercio() { return comercio; }
    public void setComercio(Usuario comercio) { this.comercio = comercio; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Double getPesoKg() { return pesoKg; }
    public void setPesoKg(Double pesoKg) { this.pesoKg = pesoKg; }

    public boolean isDisponible() { return disponible == null || disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
}
