package com.enviosp2p.Enviosp2p.entity;

import com.enviosp2p.Enviosp2p.enums.EstadoEnvio;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- RELACIONES ---

    // El usuario que quiere enviar el paquete (REMITENTE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remitente_id", nullable = false)
    private Usuario remitente;

    @Column(nullable = false)
    private String celular;

    // El usuario que viaja (VIAJERO) Debe ser null al inicio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viajero_id")
    private Usuario viajero;

    // --- DATOS DEL ENVÍO ---

    private String descripcion; // Ej: "Llaves de casa"

    private BigDecimal recompensa;


    private BigDecimal distancia;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estado;

    @Column(name = "codigo_confirmacion")
    private String codigo_confirmacion;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaAsignacion;

    private LocalDateTime fechaCierre;

    // --- LAS DIRECCIONES (EMBEDDED) ---

    // 1. ORIGEN
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "calle", column = @Column(name = "origen_calle")),
            @AttributeOverride(name = "altura", column = @Column(name = "origen_altura")),
            @AttributeOverride(name = "ciudad", column = @Column(name = "origen_ciudad")),
            @AttributeOverride(name = "provincia", column = @Column(name = "origen_provincia")),
            @AttributeOverride(name = "pais", column = @Column(name = "origen_pais")),
            @AttributeOverride(name = "latitud", column = @Column(name = "origen_lat")),
            @AttributeOverride(name = "longitud", column = @Column(name = "origen_lng"))
    })
    private Direccion origen;

    // 2. DESTINO
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "calle", column = @Column(name = "destino_calle")),
            @AttributeOverride(name = "altura", column = @Column(name = "destino_altura")),
            @AttributeOverride(name = "ciudad", column = @Column(name = "destino_ciudad")),
            @AttributeOverride(name = "provincia", column = @Column(name = "destino_provincia")),
            @AttributeOverride(name = "pais", column = @Column(name = "destino_pais")),
            @AttributeOverride(name = "latitud", column = @Column(name = "destino_lat")),
            @AttributeOverride(name = "longitud", column = @Column(name = "destino_lng"))
    })
    private Direccion destino;

    // Un metodo útil para asignar la fecha antes de guardar
    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoEnvio.PENDIENTE;
        }
    }
}