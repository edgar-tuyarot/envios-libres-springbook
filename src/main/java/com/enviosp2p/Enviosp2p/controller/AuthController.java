package com.enviosp2p.Enviosp2p.controller;

import com.enviosp2p.Enviosp2p.dto.AuthResponseDto;
import com.enviosp2p.Enviosp2p.dto.LoginRequestDto;
import com.enviosp2p.Enviosp2p.dto.RegistroRequestDto;
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



}