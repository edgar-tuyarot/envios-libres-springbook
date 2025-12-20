package com.enviosp2p.Enviosp2p.service;

import com.enviosp2p.Enviosp2p.dto.AuthResponseDto;
import com.enviosp2p.Enviosp2p.dto.LoginRequestDto;
import com.enviosp2p.Enviosp2p.dto.RegistroRequestDto;
import com.enviosp2p.Enviosp2p.entity.Usuario;
import com.enviosp2p.Enviosp2p.exceptions.InvalidTokenException;
import com.enviosp2p.Enviosp2p.exceptions.UserNotFoundException;
import com.enviosp2p.Enviosp2p.mapper.UsuarioMapper;
import com.enviosp2p.Enviosp2p.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UsuarioMapper usuarioMapper;
    private final EmailService emailService; // 1. Inyectamos el servicio de Email


    //Registro de Usuario
    public void registrarUsuario(RegistroRequestDto request) {
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        String passEncoded = passwordEncoder.encode(request.contrasena());

        // Usamos el Mapper...
        Usuario nuevoUsuario = usuarioMapper.toEntity(request, passEncoded);

        // 2. Generamos un token único (UUID es perfecto para esto)
        String tokenActivacion = UUID.randomUUID().toString();
        nuevoUsuario.setTokenActivacion(tokenActivacion);

        // Guardamos (nace inactivo y con token)
        usuarioRepository.save(nuevoUsuario);

        // 3. Enviamos el correo (OJO: Esto puede tardar unos segundos, en producción se hace asíncrono)
        emailService.enviarCorreoActivacion(nuevoUsuario.getCorreo(), tokenActivacion);
    }

    //Activacion De Cuenta
    public void activarCuenta(String token) {
        // 1. Buscamos al usuario por el token
        // (Necesitarás agregar este método en el Repository, ver Paso 2.5 abajo 👇)
        Usuario usuario = usuarioRepository.findByTokenActivacion(token)
                .orElseThrow(() -> new RuntimeException("Token inválido o expirado"));

        // 2. Activamos
        usuario.setActivo(true);
        usuario.setTokenActivacion(null); // Borramos el token para que no se use dos veces

        // 3. Guardamos cambios
        usuarioRepository.save(usuario);
    }

    //Login
    public AuthResponseDto autenticarUsuario(LoginRequestDto request) {
        // 1. Autenticamos
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo(), request.contrasena())
        );

        // 2. Buscamos al usuario (Asegúrate de importar UserDetails de Spring Security)
        var userDetails = userDetailsService.loadUserByUsername(request.correo());

        // 3. Generamos el token
        String jwtToken = jwtService.generateToken(userDetails);

        // 4. Retornamos el objeto DTO con el token dentro
        return new AuthResponseDto(jwtToken);
    }

    //Solicitud de recupero de contraseña
    public void solicitarRecuperacion(String correo) throws UserNotFoundException {
        // Buscamos usuario. Si no existe, no hacemos nada (por seguridad no avisamos "no existe")
        // O puedes lanzar excepción si prefieres ser explícito con el frontend.
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UserNotFoundException("El usuario no existe"));

        // Generamos un token aleatorio seguro
        String token = UUID.randomUUID().toString();

        // Guardamos token y fecha (ej: 15 minutos de vida)
        usuario.setTokenRecuperacion(token);
        usuario.setTokenExpiracion(LocalDateTime.now().plusMinutes(15));

        usuarioRepository.save(usuario);

        // Enviamos el correo (Aquí usas tu lógica de email ya existente)
        String link = "http://tusitio.com/reset-password?token=" + token;
        emailService.enviarCorreoRecuperacion(correo, link);
    }

    //Valida token y cambia password
    public void confirmarCambioPassword(String token, String nuevaPassword) {
        // Buscamos por el token
        Usuario usuario = usuarioRepository.findByTokenRecuperacion(token)
                .orElseThrow(() -> new InvalidTokenException("Token inválido o no encontrado"));

        // Verificamos si ya expiró
        if (usuario.getTokenExpiracion().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("El token ha expirado. Solicita uno nuevo.");
        }

        //Encriptamos la nueva contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(nuevaPassword));

        //Limpiamos el token para que no se pueda volver a usar (Seguridad)
        usuario.setTokenRecuperacion(null);
        usuario.setTokenExpiracion(null);

        usuarioRepository.save(usuario);
    }

}