package com.enviosp2p.Enviosp2p.controller;

import com.enviosp2p.Enviosp2p.dto.LoginRequestDto;
import com.enviosp2p.Enviosp2p.dto.RegistroRequestDto;
import com.enviosp2p.Enviosp2p.entity.Usuario; // Ojo, idealmente devolveríamos un DTO, pero para probar usaremos esto por ahora
import com.enviosp2p.Enviosp2p.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // ENDPOINT DE REGISTRO
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequestDto request) {
        if (usuarioRepository.existsByCorreo(request.correo())) {
            return ResponseEntity.badRequest().body("Error: El correo ya está registrado");
        }

        // Crear usuario nuevo (Aquí usamos el Builder que agregamos a la entidad)
        // Nota: Asumimos que Rol es un Enum. Si es String, ponlo como string.
        Usuario nuevoUsuario = Usuario.builder()
                .nombre(request.nombre())
                .apellido(request.apellido())
                .correo(request.correo())
                .contrasena(passwordEncoder.encode(request.contrasena())) // ¡Encriptamos la password!
                .rol(com.enviosp2p.Enviosp2p.enums.Rol.USER) // Asignamos rol por defecto
                .activo(true)
                .build();

        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    // ENDPOINT DE LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        // 1. Intentamos autenticar (Spring hace la magia aquí comparando el hash)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo(), request.contrasena())
        );

        // 2. Si no lanza excepción, es que funcionó. Guardamos la sesión en el contexto.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Retornamos éxito (Más adelante aquí devolveremos el Token JWT)
        return ResponseEntity.ok("Login exitoso: Bienvenido " + authentication.getName());
    }
}