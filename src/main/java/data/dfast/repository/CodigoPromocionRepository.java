package data.dfast.repository;

import data.dfast.model.entity.CodigoPromocion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodigoPromocionRepository extends JpaRepository<CodigoPromocion, Long> {

    Optional<CodigoPromocion> findByCodigoIgnoreCase(String codigo);

    List<CodigoPromocion> findByActivaTrue();
}
