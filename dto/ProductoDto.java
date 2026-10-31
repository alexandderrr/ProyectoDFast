package com.proyecto.proyectodfast.dto;

public class ProductoDto {
    private Long id;
    private String nombre;
    private Double precio;
    private Boolean disponible;

    public ProductoDto(Long id, String nombre, Double precio, Boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Double getPrecio() { return precio; }
    public Boolean getDisponible() { return disponible; }
}