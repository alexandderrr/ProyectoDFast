package data.dfast.repository;

import data.dfast.model.entity.Pedido;
import data.dfast.model.entity.Usuario;
import data.dfast.model.enums.EstadoPedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByRepartidorId(Long repartidorId);
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByComercioId(Long comercioId);
    List<Pedido> findByRepartidorIsNullAndEstado(EstadoPedido estado);

    // Asigna el repartidor solo si el pedido sigue libre (evita que dos lo acepten a la vez)
    @Modifying
    @Query("UPDATE Pedido p SET p.repartidor = :repartidor "
            + "WHERE p.id = :pedidoId "
            + "AND p.repartidor IS NULL "
            + "AND p.estado = :estado")
    int asignarSiDisponible(
            @Param("pedidoId") Long pedidoId,
            @Param("repartidor") Usuario repartidor,
            @Param("estado") EstadoPedido estado);
}
