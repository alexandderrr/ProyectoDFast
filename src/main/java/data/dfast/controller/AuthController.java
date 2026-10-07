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
import data.dfast.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {

        Optional<Usuario> usuarioOpt =
                usuarioService.buscarPorCorreo(request.getCorreo());

        if (usuarioOpt.isPresent()) {

            Usuario usuario = usuarioOpt.get();

            if (usuario.getPassword().equals(request.getPassword())) {
                return ResponseEntity.ok(
                    "Login exitoso. Bienvenido "
                    + usuario.getNombre()
                    + " (" + usuario.getRol() + ")"
                );
            }
        }

        return ResponseEntity.status(401)
                .body("Credenciales incorrectas");
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @RequestBody Usuario nuevoUsuario) {

        // Verificamos si el correo ya existe
        if (usuarioService.existePorCorreo(nuevoUsuario.getCorreo())) {
            return ResponseEntity.badRequest()
                    .body("Error: El correo ya está registrado");
        }

        // Registramos el usuario mediante el servicio
        Usuario guardado =
                usuarioService.registrarUsuario(nuevoUsuario);

        return ResponseEntity.ok(
                "Usuario registrado exitosamente con ID: "
                + guardado.getId()
        );
    }
}