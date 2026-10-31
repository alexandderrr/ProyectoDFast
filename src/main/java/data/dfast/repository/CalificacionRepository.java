package data.dfast.repository;

import data.dfast.model.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    List<Calificacion> findByPedidoId(Long pedidoId);
    boolean existsByPedidoIdAndUsuarioId(Long pedidoId, Long usuarioId);
}