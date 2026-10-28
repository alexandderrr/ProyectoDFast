/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.dfast.service;

/**
 *
 * @author Brandon
 */
import data.dfast.model.entity.Usuario;
import data.dfast.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import data.dfast.dto.UsuarioRespuestaDTO;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public boolean existePorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).isPresent();
    }
    public List<UsuarioRespuestaDTO> listarUsuarios() {
    return usuarioRepository.findAll()
            .stream()
            .map(usuario -> new UsuarioRespuestaDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getCorreo(),
                    usuario.getRol(),
                    usuario.getNombreComercio()
            ))
            .collect(Collectors.toList());
    }
}
