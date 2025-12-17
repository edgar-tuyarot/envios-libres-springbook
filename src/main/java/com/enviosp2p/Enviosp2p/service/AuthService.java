package com.enviosp2p.Enviosp2p.service;

import com.enviosp2p.Enviosp2p.dto.LoginRequestDto;
import com.enviosp2p.Enviosp2p.dto.RegistroRequestDto;
import com.enviosp2p.Enviosp2p.entity.Usuario;
import com.enviosp2p.Enviosp2p.mapper.UsuarioMapper;
import com.enviosp2p.Enviosp2p.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
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
    public String autenticarUsuario(LoginRequestDto request) {
        //Autenticar con el Manager de Spring
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo(), request.contrasena())
        );

        //Guardar en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Retornar algo (Por ahora el nombre, luego será el Token JWT)
        return authentication.getName();
    }
}