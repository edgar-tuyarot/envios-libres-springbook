package com.enviosp2p.Enviosp2p.auth.dto;

import lombok.Builder;

// Usamos @Builder para que sea fácil de crear
@Builder
public record AuthResponseDto(
        String token
) {}