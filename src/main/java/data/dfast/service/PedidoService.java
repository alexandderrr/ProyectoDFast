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

import data.dfast.dto.PedidoRespuestaDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

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

        if (request.getComercioId() != null) {

            Usuario comercio = usuarioRepository.findById(request.getComercioId())
                    .orElseThrow(() -> new IllegalArgumentException(
                    "El comercio indicado no existe."));

            if (!"COMERCIO".equalsIgnoreCase(comercio.getRol())) {
                throw new IllegalArgumentException(
                        "El usuario indicado no tiene el rol COMERCIO.");
            }

            nuevoPedido.setComercio(comercio);
        }

        return pedidoRepository.save(nuevoPedido);
    }
          
    // Consultar todos los pedidos para la administración
    public List<PedidoRespuestaDTO> listarTodosLosPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedido -> new PedidoRespuestaDTO(
                pedido.getId(),
                pedido.getDescripcion(),
                pedido.getPeso(),
                pedido.getDireccionRecogida(),
                pedido.getDireccionEntrega(),
                pedido.getEstado()
        ))
                .collect(Collectors.toList());
    }
    
    public List<PedidoRespuestaDTO> listarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId)
            .stream()
            .map(pedido -> new PedidoRespuestaDTO(
                    pedido.getId(),
                    pedido.getDescripcion(),
                    pedido.getPeso(),
                    pedido.getDireccionRecogida(),
                    pedido.getDireccionEntrega(),
                    pedido.getEstado()
            ))
            .collect(Collectors.toList());
}
    
    // Consultar los pedidos recibidos por un comercio
    public List<PedidoRespuestaDTO> listarPedidosPorComercio(Long comercioId) {

        Usuario comercio = usuarioRepository.findById(comercioId)
                .orElseThrow(() -> new IllegalArgumentException(
                "El comercio no existe."));

        if (!"COMERCIO".equalsIgnoreCase(comercio.getRol())) {
            throw new IllegalArgumentException(
                    "El usuario indicado no tiene el rol COMERCIO.");
        }

        return pedidoRepository.findByComercioId(comercioId)
                .stream()
                .map(pedido -> new PedidoRespuestaDTO(
                pedido.getId(),
                pedido.getDescripcion(),
                pedido.getPeso(),
                pedido.getDireccionRecogida(),
                pedido.getDireccionEntrega(),
                pedido.getEstado()
        ))
                .collect(Collectors.toList());
    }

    // Consultar pedidos disponibles para los repartidores
    public List<PedidoRespuestaDTO> listarPedidosDisponibles() {
        return pedidoRepository.findByRepartidorIsNullAndEstado("PENDIENTE")
                .stream()
                .map(pedido -> new PedidoRespuestaDTO(
                pedido.getId(),
                pedido.getDescripcion(),
                pedido.getPeso(),
                pedido.getDireccionRecogida(),
                pedido.getDireccionEntrega(),
                pedido.getEstado()
        ))
                .collect(Collectors.toList());
    }

// Permitir que un repartidor acepte un pedido disponible
    @Transactional
    public PedidoRespuestaDTO aceptarPedido(Long pedidoId, Long repartidorId) {
        Usuario repartidor = usuarioRepository.findById(repartidorId)
                .orElseThrow(() -> new IllegalArgumentException(
                "El repartidor no existe."));

        if (!"REPARTIDOR".equalsIgnoreCase(repartidor.getRol())) {
            throw new IllegalArgumentException(
                    "Solo un repartidor puede aceptar pedidos.");
        }

        int filasActualizadas = pedidoRepository.asignarSiDisponible(
                pedidoId, repartidor);

        if (filasActualizadas == 0) {
            throw new IllegalArgumentException(
                    "Este pedido ya no está disponible. Actualiza la lista.");
        }

        Pedido actualizado = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException(
                "El pedido no existe."));

        return new PedidoRespuestaDTO(
                actualizado.getId(),
                actualizado.getDescripcion(),
                actualizado.getPeso(),
                actualizado.getDireccionRecogida(),
                actualizado.getDireccionEntrega(),
                actualizado.getEstado()
        );
    }
    
        // Consultar los pedidos asignados a un repartidor
    public List<PedidoRespuestaDTO> listarPedidosPorRepartidor(
            Long repartidorId) {

        return pedidoRepository.findByRepartidorId(repartidorId)
                .stream()
                .map(pedido -> new PedidoRespuestaDTO(
                pedido.getId(),
                pedido.getDescripcion(),
                pedido.getPeso(),
                pedido.getDireccionRecogida(),
                pedido.getDireccionEntrega(),
                pedido.getEstado()
        ))
                .collect(Collectors.toList());
    }

    // Actualizar el estado de un pedido asignado al repartidor
    public PedidoRespuestaDTO actualizarEstadoPedido(
            Long pedidoId, Long repartidorId, String nuevoEstado) {

        Usuario repartidor = usuarioRepository.findById(repartidorId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "El repartidor no existe."));

        if (!"REPARTIDOR".equalsIgnoreCase(repartidor.getRol())) {
            throw new IllegalArgumentException(
                    "El usuario indicado no es un repartidor.");
        }

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "El pedido no existe."));

        if (pedido.getRepartidor() == null
                || !pedido.getRepartidor().getId().equals(repartidorId)) {
            throw new IllegalArgumentException(
                    "Este pedido no está asignado a ese repartidor.");
        }

        String estado = nuevoEstado == null
                ? ""
                : nuevoEstado.trim().toUpperCase(java.util.Locale.ROOT);

        if ("PENDIENTE".equals(pedido.getEstado())
                && "EN_RUTA".equals(estado)) {

            pedido.setEstado("EN_RUTA");

        } else if ("EN_RUTA".equals(pedido.getEstado())
                && "ENTREGADO".equals(estado)) {

            pedido.setEstado("ENTREGADO");

        } else {
            throw new IllegalArgumentException(
                    "Transición no válida. Usa PENDIENTE → EN_RUTA → ENTREGADO.");
        }

        Pedido actualizado = pedidoRepository.save(pedido);

        return new PedidoRespuestaDTO(
                actualizado.getId(),
                actualizado.getDescripcion(),
                actualizado.getPeso(),
                actualizado.getDireccionRecogida(),
                actualizado.getDireccionEntrega(),
                actualizado.getEstado()
        );
    }
    
        // Asignar un pedido a un repartidor
    public PedidoRespuestaDTO asignarRepartidor(
            Long pedidoId, Long repartidorId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "El pedido no existe."));

        Usuario repartidor = usuarioRepository.findById(repartidorId)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                        "El repartidor no existe."));

        if (!"REPARTIDOR".equalsIgnoreCase(repartidor.getRol())) {
            throw new IllegalArgumentException(
                    "El usuario indicado no tiene el rol REPARTIDOR.");
        }

        if ("ENTREGADO".equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalArgumentException(
                    "No se puede asignar un pedido que ya fue entregado.");
        }

        pedido.setRepartidor(repartidor);

        Pedido actualizado = pedidoRepository.save(pedido);

        return new PedidoRespuestaDTO(
                actualizado.getId(),
                actualizado.getDescripcion(),
                actualizado.getPeso(),
                actualizado.getDireccionRecogida(),
                actualizado.getDireccionEntrega(),
                actualizado.getEstado()
        );
    }

}