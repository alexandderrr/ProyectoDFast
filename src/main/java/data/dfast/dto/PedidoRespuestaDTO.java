/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.dfast.dto;

/**
 *
 * @author Brandon
 */

public class PedidoRespuestaDTO {

    private Long id;
    private String descripcion;
    private Double peso;
    private String direccionRecogida;
    private String direccionEntrega;
    private String estado;

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

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Double getPeso() {
        return peso;
    }

    public String getDireccionRecogida() {
        return direccionRecogida;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public String getEstado() {
        return estado;
    }
}
