package com.enviosp2p.Enviosp2p.controller;

import com.enviosp2p.Enviosp2p.dto.LoginRequestDto;
import com.enviosp2p.Enviosp2p.dto.RegistroRequestDto;
import com.enviosp2p.Enviosp2p.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    // Ahora el controller SOLO habla con el Service.
    // Ya no inyectamos ni Repository, ni PasswordEncoder, ni AuthManager aquí.
    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@RequestBody RegistroRequestDto request) {
        try {
            authService.registrarUsuario(request);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (RuntimeException e) {
            // Capturamos la excepción simple que lanzamos en el servicio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto request) {
        // Si la autenticación falla, Spring Security lanza una excepción automática (BadCredentialsException)
        // que por defecto devuelve un 401 o 403, así que aquí asumimos éxito.
        String usuarioAutenticado = authService.autenticarUsuario(request);
        return ResponseEntity.ok("Login exitoso: Bienvenido " + usuarioAutenticado);
    }
}