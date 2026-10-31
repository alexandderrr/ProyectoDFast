package com.proyecto.proyectodfast.repository;

import com.proyecto.proyectodfast.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByComercioId(Long comercioId);
}