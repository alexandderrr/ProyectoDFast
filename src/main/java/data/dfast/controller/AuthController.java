/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.dfast.controller;

/**
 *
 * @author Brandon
 */
import data.dfast.dto.LoginRequestDTO;
import data.dfast.model.entity.Usuario;
import data.dfast.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(request.getCorreo());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(request.getPassword())) {
                return ResponseEntity.ok("Login exitoso. Bienvenido " + usuario.getNombre() + " (" + usuario.getRol() + ")");
            }
        }

        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario nuevoUsuario) {
    // 1. Verificamos si el correo ya existe en la base de datos
    if (usuarioRepository.findByCorreo(nuevoUsuario.getCorreo()).isPresent()) {
        return ResponseEntity.badRequest().body("Error: El correo ya está registrado");
    }
    
    // 2. Si no existe, lo guardamos en PostgreSQL
    Usuario guardado = usuarioRepository.save(nuevoUsuario);
    
    // 3. Devolvemos un mensaje de éxito
    return ResponseEntity.ok("Usuario registrado exitosamente con ID: " + guardado.getId());
}
}