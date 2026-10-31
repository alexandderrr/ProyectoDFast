package com.proyecto.proyectodfast.service;

import com.proyecto.proyectodfast.model.Comercio;
import com.proyecto.proyectodfast.model.Producto;
import com.proyecto.proyectodfast.repository.ComercioRepository;
import com.proyecto.proyectodfast.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComercioService {
    @Autowired
    private ComercioRepository comercioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Comercio> listarComercios() {
        return comercioRepository.findAll();
    }

    public Optional<Comercio> obtenerComercioPorId(Long id) {
        return comercioRepository.findById(id);
    }

    public Comercio guardarComercio(Comercio comercio) {
        return comercioRepository.save(comercio);
    }

    public List<Producto> listarProductosPorComercio(Long comercioId) {
        return productoRepository.findByComercioId(comercioId);
    }

    public Producto agregarProductoAComercio(Long comercioId, Producto producto) {
        Comercio comercio = comercioRepository.findById(comercioId)
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado"));
        producto.setComercio(comercio);
        return productoRepository.save(producto);
    }
}