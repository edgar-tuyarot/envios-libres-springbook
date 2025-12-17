package com.enviosp2p.Enviosp2p.auth.service;

import com.enviosp2p.Enviosp2p.auth.dto.AuthResponseDto;
import com.enviosp2p.Enviosp2p.auth.dto.LoginRequestDto;
import com.enviosp2p.Enviosp2p.auth.dto.RegistroRequestDto;
import com.enviosp2p.Enviosp2p.auth.entity.Usuario;
import com.enviosp2p.Enviosp2p.auth.mapper.UsuarioMapper;
import com.enviosp2p.Enviosp2p.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UsuarioMapper usuarioMapper;

    // Lógica de Registro
    public void registrarUsuario(RegistroRequestDto request) {
        //REGLA DE NEGOCIO: Validar existencia
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        //Encriptar contraseña
        String passEncoded = passwordEncoder.encode(request.contrasena());

        //Delegamos la construcción sucia al Mapper
        Usuario nuevoUsuario = usuarioMapper.toEntity(request, passEncoded);

        //Guardar
        usuarioRepository.save(nuevoUsuario);
    }

    // Lógica de Login
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
}