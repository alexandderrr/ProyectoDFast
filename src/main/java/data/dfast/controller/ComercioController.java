package data.dfast.controller;

import data.dfast.dto.ComercioRespuestaDTO;
import data.dfast.dto.ProductoRequestDTO;
import data.dfast.dto.ProductoRespuestaDTO;
import data.dfast.service.ComercioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comercios")
public class ComercioController {

    @Autowired
    private ComercioService comercioService;

    // Listado de comercios con su calificación promedio
    @GetMapping
    public ResponseEntity<List<ComercioRespuestaDTO>> listarComercios() {
        return ResponseEntity.ok(comercioService.listarComercios());
    }

    // Productos de un comercio (todos=true incluye los no disponibles, para el dueño)
    @GetMapping("/{comercioId}/productos")
    public ResponseEntity<List<ProductoRespuestaDTO>> listarProductos(
            @PathVariable Long comercioId,
            @RequestParam(defaultValue = "false") boolean todos) {
        return ResponseEntity.ok(comercioService.listarProductos(comercioId, !todos));
    }

    @PostMapping("/{comercioId}/productos")
    public ResponseEntity<ProductoRespuestaDTO> crearProducto(
            @PathVariable Long comercioId,
            @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.ok(comercioService.crearProducto(comercioId, request));
    }

    @PatchMapping("/{comercioId}/productos/{productoId}/disponibilidad")
    public ResponseEntity<ProductoRespuestaDTO> cambiarDisponibilidad(
            @PathVariable Long comercioId,
            @PathVariable Long productoId,
            @RequestParam Long usuarioId,
            @RequestParam boolean disponible) {
        return ResponseEntity.ok(
                comercioService.cambiarDisponibilidad(comercioId, productoId, usuarioId, disponible));
    }
}
