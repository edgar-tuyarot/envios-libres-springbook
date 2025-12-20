package com.enviosp2p.Enviosp2p.controller;

import com.enviosp2p.Enviosp2p.dto.*;
import com.enviosp2p.Enviosp2p.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {



    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@RequestBody RegistroRequestDto request) {
            authService.registrarUsuario(request);
            return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        AuthResponseDto respuesta = authService.autenticarUsuario(request);
        return ResponseEntity.ok(respuesta);
    }


    @GetMapping("/activar")
    public ResponseEntity<String> activarCuenta(@RequestParam String token) {
        try {
            authService.activarCuenta(token);
            return ResponseEntity.ok("¡Cuenta activada con éxito! Ya puedes iniciar sesión.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<String> recuperarPassword(@RequestBody SolicitudRecuperacionDto dto) {
        authService.solicitarRecuperacion(dto.correo());
        return ResponseEntity.ok("Si el correo existe, se han enviado las instrucciones.");
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<String> cambiarPassword(@RequestBody CambioPasswordDto dto) {
        authService.confirmarCambioPassword(dto.token(), dto.contrasenaActual());
        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }



}