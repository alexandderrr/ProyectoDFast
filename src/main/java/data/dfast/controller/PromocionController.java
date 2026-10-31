package data.dfast.controller;

import data.dfast.dto.*;
import data.dfast.model.entity.Promocion;
import data.dfast.service.PromocionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/promociones")
public class PromocionController {
    private final PromocionService service;
    public PromocionController(PromocionService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PromocionRequestDTO request) {
        try { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.badRequest().body(ex.getMessage()); }
    }

    @GetMapping
    public List<Promocion> listar() { return service.listar(); }

    @PostMapping("/aplicar")
    public ResponseEntity<?> aplicar(@RequestBody AplicarPromocionDTO request) {
        try { return ResponseEntity.ok(service.aplicar(request)); }
        catch (IllegalArgumentException ex) { return ResponseEntity.badRequest().body(ex.getMessage()); }
    }
}