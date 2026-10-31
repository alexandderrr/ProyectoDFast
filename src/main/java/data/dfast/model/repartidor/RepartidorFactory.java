package data.dfast.model.repartidor;

import data.dfast.exception.ReglaNegocioException;
import java.util.Locale;
import java.util.Map;

/** Crea el repartidor del tipo correcto según el vehículo, sin cadenas de if/else. */
public final class RepartidorFactory {

    @FunctionalInterface
    private interface Constructor {
        Repartidor crear(Long id, String nombre, String zona, boolean disponible);
    }

    private static final Map<String, Constructor> CONSTRUCTORES = Map.of(
            "MOTO", RepartidorMoto::new,
            "BICICLETA", RepartidorBicicleta::new,
            "AUTOMOVIL", RepartidorAutomovil::new);

    private RepartidorFactory() {
    }

    public static Repartidor crear(String tipoVehiculo, Long id, String nombre,
                                   String zona, boolean disponible) {
        String tipo = (tipoVehiculo == null || tipoVehiculo.isBlank())
                ? "MOTO" : tipoVehiculo.trim().toUpperCase(Locale.ROOT);
        Constructor constructor = CONSTRUCTORES.get(tipo);
        if (constructor == null) {
            throw new ReglaNegocioException("Tipo de vehículo no válido: " + tipoVehiculo);
        }
        return constructor.crear(id, nombre, zona, disponible);
    }
}
