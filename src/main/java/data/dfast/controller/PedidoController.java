/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.dfast.controller;

/**
 *
 * @author Brandon
 */
import data.dfast.dto.PedidoRequestDTO;
import data.dfast.model.entity.Pedido;
import data.dfast.model.entity.Usuario;
import data.dfast.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import data.dfast.dto.PedidoRespuestaDTO;
import java.util.List;

import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<?> crearPedido(
            @RequestBody PedidoRequestDTO request) {

        // 1. Verificamos que el cliente exista
        Optional<Usuario> clienteOpt =
                pedidoService.buscarCliente(request.getClienteId());

        if (clienteOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Error: El cliente con ID "
                            + request.getClienteId()
                            + " no existe.");
        }
        
        if (!"CLIENTE".equalsIgnoreCase(clienteOpt.get().getRol())) {
            return ResponseEntity.badRequest()
                    .body("Solo los usuarios con rol CLIENTE pueden crear pedidos.");
        }

        // 2. Creamos el pedido mediante el servicio
        Pedido guardado =
                pedidoService.crearPedido(request, clienteOpt.get());

        // 3. Devolvemos el resultado
        return ResponseEntity.ok(
                "Pedido creado exitosamente con ID: " + guardado.getId()
                        + ". Total a pagar: Q" + String.format("%.2f", guardado.getTotal())
                        + " (" + guardado.getMetodoPago() + ")"
        );
    }
    
    // Consultar todos los pedidos para el administrador
    @GetMapping
    public ResponseEntity<List<PedidoRespuestaDTO>> listarTodosLosPedidos() {
        return ResponseEntity.ok(
                pedidoService.listarTodosLosPedidos()
        );
    }
    
    // Consultar los pedidos recibidos por un comercio
    @GetMapping("/comercio/{comercioId}")
    public ResponseEntity<?> listarPedidosPorComercio(
            @PathVariable Long comercioId) {

        try {
            return ResponseEntity.ok(
                    pedidoService.listarPedidosPorComercio(comercioId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
      
    @GetMapping("/cliente/{clienteId}")
        public ResponseEntity<List<PedidoRespuestaDTO>> listarPedidosPorCliente(
            @PathVariable Long clienteId) {

        List<PedidoRespuestaDTO> pedidos =
            pedidoService.listarPedidosPorCliente(clienteId);

        return ResponseEntity.ok(pedidos);
}
        
        // Consultar pedidos disponibles
        @GetMapping("/disponibles")
        public ResponseEntity<?> listarPedidosDisponibles() {
            return ResponseEntity.ok(
                    pedidoService.listarPedidosDisponibles());
        }

        // Aceptar un pedido disponible
            @PatchMapping("/{pedidoId}/aceptar")
            public ResponseEntity<?> aceptarPedido(
                    @PathVariable Long pedidoId,
                    @RequestParam Long repartidorId) {
                try {
                    return ResponseEntity.ok(
                            pedidoService.aceptarPedido(pedidoId, repartidorId));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }

            // Consultar las entregas asignadas a un repartidor
    @GetMapping("/repartidor/{repartidorId}")
    public ResponseEntity<?> listarPedidosPorRepartidor(
            @PathVariable Long repartidorId) {

        Optional<Usuario> repartidor
                = pedidoService.buscarCliente(repartidorId);

        if (repartidor.isEmpty()
                || !"REPARTIDOR".equalsIgnoreCase(
                        repartidor.get().getRol())) {
            return ResponseEntity.badRequest()
                    .body("El usuario indicado no es un repartidor válido.");
        }

        return ResponseEntity.ok(
                pedidoService.listarPedidosPorRepartidor(repartidorId));
    }

    // Actualizar el estado de una entrega
    @PatchMapping("/{pedidoId}/estado")
    public ResponseEntity<?> actualizarEstadoPedido(
            @PathVariable Long pedidoId,
            @RequestParam Long repartidorId,
            @RequestParam String estado) {

        try {
            return ResponseEntity.ok(
                    pedidoService.actualizarEstadoPedido(
                            pedidoId, repartidorId, estado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
        // Asignar un pedido a un repartidor
    @PatchMapping("/{pedidoId}/asignar-repartidor")
    public ResponseEntity<?> asignarRepartidor(
            @PathVariable Long pedidoId,
            @RequestParam Long repartidorId) {

        try {
            return ResponseEntity.ok(
                    pedidoService.asignarRepartidor(
                            pedidoId, repartidorId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    // Cambiar el estado de un pedido (comercio, cliente, repartidor o admin)
    @PutMapping("/{pedidoId}/estado")
    public ResponseEntity<PedidoRespuestaDTO> cambiarEstado(
            @PathVariable Long pedidoId,
            @RequestParam Long usuarioId,
            @RequestParam String estado) {

        return ResponseEntity.ok(
                pedidoService.actualizarEstado(pedidoId, usuarioId, estado));
    }

    // Seguimiento de un pedido (usa la interfaz Rastreable)
    @GetMapping("/{pedidoId}/seguimiento")
    public ResponseEntity<java.util.Map<String, String>> seguimiento(@PathVariable Long pedidoId) {
        Pedido pedido = pedidoService.buscarPedidoPorId(pedidoId);
        return ResponseEntity.ok(java.util.Map.of(
                "estado", pedido.getEstado().name(),
                "seguimiento", pedido.consultarUbicacion()));
    }

}
