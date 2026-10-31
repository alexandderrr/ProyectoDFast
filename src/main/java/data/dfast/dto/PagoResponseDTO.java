package data.dfast.dto;

import data.dfast.model.entity.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoResponseDTO {
    private Long id;
    private String tipo;
    private BigDecimal monto;
    private Long pedidoId;
    private LocalDateTime fechaPago;
    private BigDecimal cambio;
    private String referencia;

    public PagoResponseDTO(Pago pago) {
        id = pago.getId();
        monto = pago.getMonto();
        pedidoId = pago.getPedido().getId();
        fechaPago = pago.getFechaPago();
        if (pago instanceof PagoEfectivo efectivo) {
            tipo = "EFECTIVO";
            cambio = efectivo.getCambio();
        } else if (pago instanceof PagoTarjeta tarjeta) {
            tipo = "TARJETA";
            referencia = "****" + tarjeta.getUltimosCuatro();
        } else if (pago instanceof PagoTransferencia transferencia) {
            tipo = "TRANSFERENCIA";
            referencia = transferencia.getReferenciaTransferencia();
        }
    }
    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public BigDecimal getMonto() { return monto; }
    public Long getPedidoId() { return pedidoId; }
    public LocalDateTime getFechaPago() { return fechaPago; }
    public BigDecimal getCambio() { return cambio; }
    public String getReferencia() { return referencia; }
}