package com.enviosp2p.Enviosp2p.auth.service;

import com.enviosp2p.Enviosp2p.auth.entity.Usuario;
import com.enviosp2p.Enviosp2p.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // 1. Buscamos el usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // 2. Traducimos Rol a un Authority de Spring Security
        // Spring espera que los roles empiecen con "ROLE_" por convención
        Collection<? extends GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
        );
        // 3. Retornamos un objeto User de Spring Security con los datos de TU usuario
                return new User(
                        usuario.getCorreo(),
                        usuario.getContrasena(),
                        usuario.getActivo(), // enabled (si está activo)
                        true, // accountNonExpired
                        true, // credentialsNonExpired
                        true, // accountNonLocked
                        authorities
                );
    }



}
