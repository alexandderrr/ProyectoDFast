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
import data.dfast.repository.PedidoRepository;
import data.dfast.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody PedidoRequestDTO request) {
        // 1. Verificamos que el cliente exista
        Optional<Usuario> clienteOpt = usuarioRepository.findById(request.getClienteId());

        if (clienteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El cliente con ID " + request.getClienteId() + " no existe.");
        }

        // 2. Construimos el objeto Pedido a partir de los datos del DTO
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setDescripcion(request.getDescripcion());
        nuevoPedido.setPeso(request.getPeso());
        nuevoPedido.setDireccionRecogida(request.getDireccionRecogida());
        nuevoPedido.setDireccionEntrega(request.getDireccionEntrega());
        nuevoPedido.setCliente(clienteOpt.get()); 
        // Nota: El estado por defecto ya es "PENDIENTE" y el repartidor queda en null automáticamente.

        // 3. Guardamos en la base de datos
        Pedido guardado = pedidoRepository.save(nuevoPedido);

        return ResponseEntity.ok("Pedido creado exitosamente con ID: " + guardado.getId());
    }
}