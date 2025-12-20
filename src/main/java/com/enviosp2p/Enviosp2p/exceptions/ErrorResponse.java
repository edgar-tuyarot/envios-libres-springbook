package com.enviosp2p.Enviosp2p.exceptions;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String codigo;      // Ej: P-404, P-500
    private String mensaje;     // "El usuario no existe"
    private String detalles;    // Descripción técnica (opcional)
    private LocalDateTime fecha; // Cuándo ocurrió
}