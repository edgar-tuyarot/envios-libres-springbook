package com.enviosp2p.Enviosp2p.dto.envios;

import com.enviosp2p.Enviosp2p.dto.usuarios.UsuarioResponseDto;
import com.enviosp2p.Enviosp2p.enums.EstadoEnvio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MiEnvioDto(
        Long id,
        UsuarioResponseDto remitente,
        UsuarioResponseDto viajero,
        String descripcion,
        DireccionDto origen,
        DireccionDto destino,
        EstadoEnvio estado,
        String codigo_confirmacion,
        BigDecimal recompensa,
        BigDecimal distancia,
        LocalDateTime fecha_creacion,
        LocalDateTime fecha_asignacion,
        LocalDateTime fecha_cierre
) {
}
