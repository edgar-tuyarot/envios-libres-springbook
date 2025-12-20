package com.enviosp2p.Enviosp2p.dto;

import lombok.Builder;

// Usamos @Builder para que sea fácil de crear
@Builder
public record AuthResponseDto(
        String token
) {}