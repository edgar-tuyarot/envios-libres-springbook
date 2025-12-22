package com.enviosp2p.Enviosp2p.dto.envios;

import jakarta.validation.constraints.NotBlank;

public record DireccionDto(
        @NotBlank(message = "La calle es obligatoria")
        String calle,

        @NotBlank(message = "La altura es obligatoria")
        String altura,

        @NotBlank(message = "La ciudad es obligatoria")
        String ciudad,

        @NotBlank(message = "La provincia es obligatoria")
        String provincia,

        @NotBlank(message = "El país es obligatorio")
        String pais,
        // Las coordenadas pueden ser null, no ponemos NotBlank
        Double latitud,
        Double longitud
) {}