/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.dfast.service;

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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> buscarCliente(Long clienteId) {
        return usuarioRepository.findById(clienteId);
    }

    public Pedido crearPedido(PedidoRequestDTO request, Usuario cliente) {

        Pedido nuevoPedido = new Pedido();

        nuevoPedido.setDescripcion(request.getDescripcion());
        nuevoPedido.setPeso(request.getPeso());
        nuevoPedido.setDireccionRecogida(request.getDireccionRecogida());
        nuevoPedido.setDireccionEntrega(request.getDireccionEntrega());
        nuevoPedido.setCliente(cliente);

        return pedidoRepository.save(nuevoPedido);
    }
}
//Uso de implementacioo