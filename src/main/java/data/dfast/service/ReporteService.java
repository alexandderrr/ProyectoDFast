package data.dfast.service;

import data.dfast.model.entity.Pedido;
import data.dfast.model.entity.Usuario;
import data.dfast.model.enums.EstadoPedido;
import data.dfast.repository.PedidoRepository;
import data.dfast.repository.UsuarioRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReporteService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Map<String, Object> resumenGeneral() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<Usuario> usuarios = usuarioRepository.findAll();

        Map<String, Long> porEstado = pedidos.stream().collect(Collectors.groupingBy(
                p -> p.getEstado().name(), TreeMap::new, Collectors.counting()));

        Map<String, Long> usuariosPorRol = usuarios.stream().collect(Collectors.groupingBy(
                u -> u.getRol().toUpperCase(), TreeMap::new, Collectors.counting()));

        double ventas = pedidos.stream()
                .filter(p -> p.getEstado() == EstadoPedido.ENTREGADO && p.getTotal() != null)
                .mapToDouble(Pedido::getTotal)
                .sum();

        Map<String, Object> resumen = new LinkedHashMap<>();
        resumen.put("totalPedidos", pedidos.size());
        resumen.put("pedidosPorEstado", porEstado);
        resumen.put("ventasEntregadas", Math.round(ventas * 100) / 100.0);
        resumen.put("totalUsuarios", usuarios.size());
        resumen.put("usuariosPorRol", usuariosPorRol);
        return resumen;
    }
}
