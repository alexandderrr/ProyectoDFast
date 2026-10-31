package data.dfast.controller;

import data.dfast.dto.CalificacionRequestDTO;
import data.dfast.model.entity.Calificacion;
import data.dfast.service.CalificacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {
    private final CalificacionService service;
    public CalificacionController(CalificacionService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CalificacionRequestDTO request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<Calificacion> listarPorPedido(@PathVariable Long pedidoId) {
        return service.listarPorPedido(pedidoId);
    }
}