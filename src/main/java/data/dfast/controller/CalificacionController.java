package data.dfast.controller;

import data.dfast.dto.CalificacionRequestDTO;
import data.dfast.dto.CalificacionRespuestaDTO;
import data.dfast.dto.PedidoRespuestaDTO;
import data.dfast.service.CalificacionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;

    // Calificación del comercio y del repartidor de un pedido entregado
    @PostMapping
    public ResponseEntity<CalificacionRespuestaDTO> registrar(
            @RequestBody CalificacionRequestDTO request) {
        return ResponseEntity.ok(calificacionService.registrar(request));
    }

    @GetMapping("/pendientes/{clienteId}")
    public ResponseEntity<List<PedidoRespuestaDTO>> pendientes(@PathVariable Long clienteId) {
        return ResponseEntity.ok(calificacionService.listarPendientes(clienteId));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CalificacionRespuestaDTO>> delCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(calificacionService.listarPorCliente(clienteId));
    }
}
