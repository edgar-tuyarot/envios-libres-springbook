package com.enviosp2p.Enviosp2p.dto.envios;

import com.enviosp2p.Enviosp2p.dto.usuarios.UsuarioResponseDto;
import com.enviosp2p.Enviosp2p.enums.EstadoEnvio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EnvioDto(
        Long id,
        UsuarioResponseDto remitente,
        String celular,
        String descripcion,
        DireccionDto origen,
        DireccionDto destino,
        EstadoEnvio estado,
        BigDecimal recompensa,
        BigDecimal distancia,
        LocalDateTime fecha_creacion
) {
}
