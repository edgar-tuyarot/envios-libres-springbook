package com.enviosp2p.Enviosp2p.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record SolicitudRecuperacionDto(
        @NotBlank
        String correo) {
}
