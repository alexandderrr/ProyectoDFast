/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.dfast.dto;

/**
 *
 * @author Brandon
 */
public class UsuarioRespuestaDTO {

    private Long id;
    private String nombre;
    private String correo;
    private String rol;
    private String nombreComercio;
    

    public UsuarioRespuestaDTO(
            Long id,
            String nombre,
            String correo,
            String rol,
            String nombreComercio) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.nombreComercio = nombreComercio;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol() {
        return rol;
    }
    
    public String getNombreComercio() {
        return nombreComercio;
    }
}