package data.dfast.service;

import data.dfast.dto.PagoRequestDTO;
import data.dfast.model.entity.*;
import data.dfast.repository.PagoRepository;
import data.dfast.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PagoService {
    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;

    public PagoService(PagoRepository pagoRepository, PedidoRepository pedidoRepository) {
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public Pago registrar(PagoRequestDTO request) {
        if (request.getMonto() == null || request.getMonto().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        if (request.getPedidoId() == null)
            throw new IllegalArgumentException("Debe indicar el ID del pedido.");

        Pedido pedido = pedidoRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new IllegalArgumentException("No existe el pedido indicado."));
        String tipo = request.getTipo() == null ? "" : request.getTipo().trim().toUpperCase();
        Pago pago;

        switch (tipo) {
            case "EFECTIVO" -> {
                if (request.getEfectivoRecibido() == null ||
                    request.getEfectivoRecibido().compareTo(request.getMonto()) < 0)
                    throw new IllegalArgumentException("El efectivo recibido debe cubrir el monto del pago.");
                PagoEfectivo p = new PagoEfectivo();
                p.setEfectivoRecibido(request.getEfectivoRecibido());
                pago = p;
            }
            case "TARJETA" -> {
                if (request.getTitularTarjeta() == null || request.getTitularTarjeta().isBlank())
                    throw new IllegalArgumentException("Debe indicar el titular de la tarjeta.");
                if (request.getUltimosCuatro() == null || !request.getUltimosCuatro().matches("\\d{4}"))
                    throw new IllegalArgumentException("Debe indicar únicamente los últimos cuatro dígitos de la tarjeta.");
                PagoTarjeta p = new PagoTarjeta();
                p.setTitularTarjeta(request.getTitularTarjeta().trim());
                p.setUltimosCuatro(request.getUltimosCuatro());
                pago = p;
            }
            case "TRANSFERENCIA" -> {
                if (request.getReferenciaTransferencia() == null || request.getReferenciaTransferencia().isBlank())
                    throw new IllegalArgumentException("La referencia de transferencia es obligatoria.");
                PagoTransferencia p = new PagoTransferencia();
                p.setReferenciaTransferencia(request.getReferenciaTransferencia().trim());
                pago = p;
            }
            default -> throw new IllegalArgumentException("Tipo de pago inválido. Use EFECTIVO, TARJETA o TRANSFERENCIA.");
        }
        pago.setMonto(request.getMonto());
        pago.setPedido(pedido);
        return pagoRepository.save(pago);
    }

    public List<Pago> listar() { return pagoRepository.findAll(); }
    public List<Pago> listarPorPedido(Long pedidoId) { return pagoRepository.findByPedidoId(pedidoId); }
}