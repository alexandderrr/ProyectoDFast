package data.dfast.config;

import data.dfast.model.entity.CodigoPromocion;
import data.dfast.repository.CodigoPromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Crea unas promociones de ejemplo la primera vez que se arranca el sistema. */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CodigoPromocionRepository promocionRepository;

    @Override
    public void run(String... args) {
        if (promocionRepository.count() > 0) {
            return;
        }
        promocionRepository.save(new CodigoPromocion("BIENVENIDO10", "PORCENTAJE", 10.0));
        promocionRepository.save(new CodigoPromocion("AHORRA15", "MONTO_FIJO", 15.0));
        promocionRepository.save(new CodigoPromocion("ENVIOGRATIS", "ENVIO_GRATIS", 0.0));
    }
}
