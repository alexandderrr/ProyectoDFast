package com.proyecto.proyectodfast.controller;

import com.proyecto.proyectodfast.model.Comercio;
import com.proyecto.proyectodfast.model.Producto;
import com.proyecto.proyectodfast.service.ComercioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comercios")
public class ComercioController {
    @Autowired
    private ComercioService comercioService;

    @GetMapping
    public List<Comercio> listarComercios() {
        return comercioService.listarComercios();
    }

    @PostMapping
    public Comercio crearComercio(@RequestBody Comercio comercio) {
        return comercioService.guardarComercio(comercio);
    }

    @GetMapping("/{id}/productos")
    public ResponseEntity<List<Producto>> listarProductosDeComercio(@PathVariable Long id) {
        List<Producto> productos = comercioService.listarProductosPorComercio(id);
        return ResponseEntity.ok(productos);
    }

    @PostMapping("/{id}/productos")
    public ResponseEntity<Producto> agregarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto nuevoProducto = comercioService.agregarProductoAComercio(id, producto);
        return ResponseEntity.ok(nuevoProducto);
    }
}