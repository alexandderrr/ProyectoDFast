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

        // 2. Creamos el pedido mediante el servicio
        Pedido guardado =
                pedidoService.crearPedido(request, clienteOpt.get());

        // 3. Devolvemos el resultado
        return ResponseEntity.ok(
                "Pedido creado exitosamente con ID: "
                        + guardado.getId()
        );
    }
}