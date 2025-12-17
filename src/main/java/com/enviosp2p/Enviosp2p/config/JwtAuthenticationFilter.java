package com.enviosp2p.Enviosp2p.config;

import com.enviosp2p.Enviosp2p.auth.service.JwtService; // Ojo a los nuevos imports
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Obtener el header de autorización
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Validar que el header exista y empiece por "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token (quitamos "Bearer " que son 7 caracteres)
        jwt = authHeader.substring(7);

        // 4. Extraer el email del token
        userEmail = jwtService.extractUsername(jwt);

        // 5. Si hay email y el usuario no está autenticado todavía en el contexto
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Buscamos al usuario en BD
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Si el token es válido, actualizamos el contexto de seguridad
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // AQUÍ OCURRE LA MAGIA: Le decimos a Spring "Este usuario está logueado"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 7. Pasar al siguiente filtro
        filterChain.doFilter(request, response);
    }
}