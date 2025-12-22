package com.enviosp2p.Enviosp2p.dto.envios;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record CrearEnvioDto(
        @NotBlank(message = "La descripción no puede estar vacía")
        String descripcion,

        @NotNull(message = "Debes ofrecer una recompensa")
        @Min(value = 1, message = "La recompensa debe ser mayor a 0")
        BigDecimal recompensa,

        @NotNull(message = "Numero de contacto obligatorio")
        String celular,

        @NotNull(message = "Faltan datos del origen")
        @Valid
        DireccionDto origen,

        @NotNull(message = "Faltan datos del destino")
        @Valid
        DireccionDto destino,

        @NotNull(message = "Falta distancia")
        @Valid
        BigDecimal distancia

) {}