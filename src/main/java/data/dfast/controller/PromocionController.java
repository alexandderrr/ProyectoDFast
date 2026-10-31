package data.dfast.controller;

import data.dfast.dto.PromocionRequestDTO;
import data.dfast.dto.PromocionRespuestaDTO;
import data.dfast.service.PromocionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promociones")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    @GetMapping
    public ResponseEntity<List<PromocionRespuestaDTO>> listar(
            @RequestParam(defaultValue = "true") boolean soloActivas) {
        return ResponseEntity.ok(promocionService.listar(soloActivas));
    }

    @PostMapping
    public ResponseEntity<PromocionRespuestaDTO> crear(@RequestBody PromocionRequestDTO request) {
        return ResponseEntity.ok(promocionService.crear(request));
    }
}
