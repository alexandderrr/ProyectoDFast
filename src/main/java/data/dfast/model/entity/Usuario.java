package data.dfast.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol;

    @Column(name = "nombre_comercio")
    private String nombreComercio;

    // Solo aplican a repartidores (MOTO, BICICLETA, AUTOMOVIL)
    @Column(name = "tipo_vehiculo")
    private String tipoVehiculo;

    private String zona;

    // Wrapper (no boolean) para que las filas viejas con NULL no den error
    private Boolean disponible = Boolean.TRUE;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getNombreComercio() { return nombreComercio; }
    public void setNombreComercio(String nombreComercio) { this.nombreComercio = nombreComercio; }

    public String getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public boolean isDisponible() { return disponible == null || disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
}
