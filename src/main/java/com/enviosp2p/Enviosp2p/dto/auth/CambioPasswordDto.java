package com.enviosp2p.Enviosp2p.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record CambioPasswordDto(
        @NotBlank(message = "El token es obligatorio")
        String token,
        @NotBlank(message = "La contraseña actual es obligatoria")
        String contrasenaActual
) {
}
