package com.enviosp2p.Enviosp2p.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable //Con esta notacion, definimo que es parte de otra tabla.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {

    private String calle;
    private String altura;
    private String ciudad;
    private String provincia;
    private String pais;

    // Coordenadas para el mapa
    private Double latitud;
    private Double longitud;
}