package com.enviosp2p.Enviosp2p.auth.controller;

import com.enviosp2p.Enviosp2p.auth.dto.AuthResponseDto;
import com.enviosp2p.Enviosp2p.auth.dto.LoginRequestDto;
import com.enviosp2p.Enviosp2p.auth.dto.RegistroRequestDto;
import com.enviosp2p.Enviosp2p.auth.service.AuthService;
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
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginRequestDto request) {
        //el servicio nos devuelve el objeto con el token
        AuthResponseDto respuesta = authService.autenticarUsuario(request);

        // Lo entregamos en el cuerpo de la respuesta (Status 200 OK)
        return ResponseEntity.ok(respuesta);
    }
}