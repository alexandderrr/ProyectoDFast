package data.dfast.service;

import data.dfast.dto.*;
import data.dfast.model.entity.*;
import data.dfast.repository.PromocionRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PromocionService {
    private final PromocionRepository repository;
    public PromocionService(PromocionRepository repository) { this.repository = repository; }

    public Promocion crear(PromocionRequestDTO request) {
        if (request.getCodigo() == null || request.getCodigo().isBlank())
            throw new IllegalArgumentException("El código de promoción es obligatorio.");
        if (request.getDescripcion() == null || request.getDescripcion().isBlank())
            throw new IllegalArgumentException("La descripción es obligatoria.");
        if (request.getTipo() == null)
            throw new IllegalArgumentException("Debe indicar el tipo de promoción.");
        TipoPromocion tipo;
        try { tipo = TipoPromocion.valueOf(request.getTipo().trim().toUpperCase()); }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo inválido. Use PORCENTAJE, MONTO_FIJO o ENVIO_GRATIS.");
        }
        BigDecimal valor = request.getValor();
        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("El valor no puede ser negativo.");
        if (tipo == TipoPromocion.PORCENTAJE &&
            (valor.compareTo(BigDecimal.ZERO) <= 0 || valor.compareTo(new BigDecimal("100")) > 0))
            throw new IllegalArgumentException("El porcentaje debe ser mayor que 0 y máximo 100.");
        if (tipo == TipoPromocion.MONTO_FIJO && valor.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El descuento fijo debe ser mayor que cero.");
        if (tipo == TipoPromocion.ENVIO_GRATIS) valor = BigDecimal.ZERO;
        if (repository.findByCodigoIgnoreCase(request.getCodigo().trim()).isPresent())
            throw new IllegalArgumentException("Ya existe una promoción con ese código.");

        Promocion p = new Promocion();
        p.setCodigo(request.getCodigo().trim().toUpperCase());
        p.setDescripcion(request.getDescripcion().trim());
        p.setTipo(tipo);
        p.setValor(valor);
        p.setActiva(true);
        return repository.save(p);
    }

    public List<Promocion> listar() { return repository.findAll(); }

    public PromocionResultadoDTO aplicar(AplicarPromocionDTO request) {
        if (request.getCodigo() == null || request.getCodigo().isBlank())
            throw new IllegalArgumentException("Debe indicar el código de promoción.");
        BigDecimal subtotal = request.getSubtotal();
        BigDecimal envio = request.getCostoEnvio();
        if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) < 0 ||
            envio == null || envio.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Subtotal y costo de envío deben ser valores no negativos.");
        Promocion p = repository.findByCodigoIgnoreCase(request.getCodigo().trim())
                .filter(Promocion::isActiva)
                .orElseThrow(() -> new IllegalArgumentException("La promoción no existe o está inactiva."));
        BigDecimal descuento = BigDecimal.ZERO;
        BigDecimal envioFinal = envio;
        switch (p.getTipo()) {
            case PORCENTAJE -> descuento = subtotal.multiply(p.getValor())
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            case MONTO_FIJO -> descuento = p.getValor().min(subtotal);
            case ENVIO_GRATIS -> envioFinal = BigDecimal.ZERO;
        }
        BigDecimal total = subtotal.subtract(descuento).add(envioFinal).setScale(2, RoundingMode.HALF_UP);
        return new PromocionResultadoDTO(p.getCodigo(), descuento, subtotal, envioFinal, total);
    }
}