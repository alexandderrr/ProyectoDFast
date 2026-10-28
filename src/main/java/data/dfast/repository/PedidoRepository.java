/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package data.dfast.repository;

/**
 *
 * @author Brandon
 */
import data.dfast.model.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import data.dfast.model.entity.Usuario;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Métodos automáticos para buscar pedidos según el PDF
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByRepartidorId(Long repartidorId);
    List<Pedido> findByEstado(String estado);
    List<Pedido> findByComercioId(Long comercioId);
    List<Pedido> findByRepartidorIsNullAndEstado(String estado);
    
    @Modifying
    @Query("UPDATE Pedido p SET p.repartidor = :repartidor "
            + "WHERE p.id = :pedidoId "
            + "AND p.repartidor IS NULL "
            + "AND p.estado = 'PENDIENTE'")
    int asignarSiDisponible(
            @Param("pedidoId") Long pedidoId,
            @Param("repartidor") Usuario repartidor);
}