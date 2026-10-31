package data.dfast.repository;

import data.dfast.model.entity.Calificacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    boolean existsByPedidoId(Long pedidoId);

    List<Calificacion> findByClienteId(Long clienteId);

    long countByComercioId(Long comercioId);

    long countByRepartidorId(Long repartidorId);

    @Query("SELECT AVG(c.puntosComercio) FROM Calificacion c WHERE c.comercio.id = :id")
    Double promedioComercio(@Param("id") Long comercioId);

    @Query("SELECT AVG(c.puntosRepartidor) FROM Calificacion c WHERE c.repartidor.id = :id")
    Double promedioRepartidor(@Param("id") Long repartidorId);
}
