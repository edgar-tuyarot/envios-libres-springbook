package com.enviosp2p.Enviosp2p.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    // 👇 Esta anotación es el "Cerrojo"
    // Solo deja pasar si el Usuario tiene en su lista de autoridades "ROLE_ADMIN"
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String dashboardAdmin() {
        return "👮‍♂️ Hola Admin. Si lees esto, tienes superpoderes.";
    }

    @GetMapping("/quien-soy")
    public Map<String, Object> checkUser(Authentication authentication) {
        if (authentication == null) {
            return Map.of("error", "No hay usuario autenticado en el contexto");
        }
        return Map.of(
                "Usuario", authentication.getName(),
                "Autoridades (Lo que ve Spring)", authentication.getAuthorities(),
                "Clase Principal", authentication.getPrincipal().getClass().getName()
        );
    }

}