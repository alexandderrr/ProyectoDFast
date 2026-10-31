package data.dfast.model.promocion;

import data.dfast.exception.ReglaNegocioException;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;

/** Crea la Promocion correcta según su tipo, sin cadenas de if/else. */
public final class PromocionFactory {

    private static final Map<String, BiFunction<String, Double, Promocion>> CREADORES = Map.of(
            "PORCENTAJE", PromocionPorcentaje::new,
            "MONTO_FIJO", PromocionMontoFijo::new,
            "ENVIO_GRATIS", (codigo, valor) -> new PromocionEnvioGratis(codigo));

    private PromocionFactory() {
    }

    public static Promocion crear(String codigo, String tipo, Double valor) {
        String clave = tipo == null ? "" : tipo.trim().toUpperCase(Locale.ROOT);
        BiFunction<String, Double, Promocion> creador = CREADORES.get(clave);
        if (creador == null) {
            throw new ReglaNegocioException(
                    "Tipo de promoción no válido (usa PORCENTAJE, MONTO_FIJO o ENVIO_GRATIS).");
        }
        return creador.apply(codigo, valor == null ? 0.0 : valor);
    }
}
