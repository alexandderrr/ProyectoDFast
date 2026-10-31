package data.dfast.repository;

import data.dfast.model.entity.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PromocionRepository extends JpaRepository<Promocion, Long> {
    Optional<Promocion> findByCodigoIgnoreCase(String codigo);
}