package data.dfast.service;

import data.dfast.dto.PromocionRequestDTO;
import data.dfast.dto.PromocionRespuestaDTO;
import data.dfast.exception.RecursoNoEncontradoException;
import data.dfast.exception.ReglaNegocioException;
import data.dfast.model.entity.CodigoPromocion;
import data.dfast.model.entity.Usuario;
import data.dfast.model.promocion.Promocion;
import data.dfast.model.promocion.PromocionFactory;
import data.dfast.repository.CodigoPromocionRepository;
import data.dfast.repository.UsuarioRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromocionService {

    @Autowired
    private CodigoPromocionRepository promocionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<PromocionRespuestaDTO> listar(boolean soloActivas) {
        List<CodigoPromocion> promociones = soloActivas
                ? promocionRepository.findByActivaTrue()
                : promocionRepository.findAll();
        return promociones.stream().map(PromocionService::aDTO).toList();
    }

    @Transactional
    public PromocionRespuestaDTO crear(PromocionRequestDTO request) {
        Usuario actor = usuarioRepository.findById(
                request.usuarioId() == null ? -1L : request.usuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no existe."));
        if (!"ADMIN".equalsIgnoreCase(actor.getRol())) {
            throw new ReglaNegocioException("Solo el administrador puede crear promociones.");
        }
        if (request.codigo() == null || request.codigo().isBlank()) {
            throw new ReglaNegocioException("La promoción necesita un código.");
        }
        String codigo = request.codigo().trim().toUpperCase(Locale.ROOT);
        if (promocionRepository.findByCodigoIgnoreCase(codigo).isPresent()) {
            throw new ReglaNegocioException("Ya existe una promoción con el código " + codigo + ".");
        }

        // La fábrica valida tipo y valor (lanza ReglaNegocioException si algo no sirve)
        PromocionFactory.crear(codigo, request.tipo(), request.valor());

        CodigoPromocion nueva = new CodigoPromocion(
                codigo, request.tipo().trim().toUpperCase(Locale.ROOT), request.valor());
        return aDTO(promocionRepository.save(nueva));
    }

    private static PromocionRespuestaDTO aDTO(CodigoPromocion c) {
        Promocion promocion = PromocionFactory.crear(c.getCodigo(), c.getTipo(), c.getValor());
        return new PromocionRespuestaDTO(
                c.getCodigo(), c.getTipo(), c.getValor(), promocion.getDescripcion(), c.isActiva());
    }
}
