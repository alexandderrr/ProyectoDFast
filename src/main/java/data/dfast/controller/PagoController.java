package data.dfast.controller;

import data.dfast.dto.PagoRequestDTO;
import data.dfast.dto.PagoResponseDTO;
import data.dfast.model.entity.Pago;
import data.dfast.service.PagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    private final PagoService pagoService;
    public PagoController(PagoService pagoService) { this.pagoService = pagoService; }

    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody PagoRequestDTO request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new PagoResponseDTO(pagoService.registrar(request)));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping
    public List<PagoResponseDTO> listar() {
        return pagoService.listar().stream().map(PagoResponseDTO::new).toList();
    }

    @GetMapping("/pedido/{pedidoId}")
    public List<PagoResponseDTO> listarPorPedido(@PathVariable Long pedidoId) {
        return pagoService.listarPorPedido(pedidoId).stream().map(PagoResponseDTO::new).toList();
    }
}