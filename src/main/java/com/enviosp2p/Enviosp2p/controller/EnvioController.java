package com.enviosp2p.Enviosp2p.controller;

import com.enviosp2p.Enviosp2p.dto.envios.CrearEnvioDto;
import com.enviosp2p.Enviosp2p.dto.envios.EnvioDto;
import com.enviosp2p.Enviosp2p.dto.envios.MiEnvioDto;
import com.enviosp2p.Enviosp2p.entity.Envio;
import com.enviosp2p.Enviosp2p.mapper.EnvioMapper;
import com.enviosp2p.Enviosp2p.service.EnvioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;
    private EnvioMapper envioMapper;


    // Ver el pedido asignado al usuario
    @GetMapping("/mi-pedido-asignado")
    public ResponseEntity<List<EnvioDto>> obtenerPorId() {
        return ResponseEntity.ok(envioService.verMiEnvioAsignado());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioDto> verEnvido(@PathVariable Long id){
        return ResponseEntity.ok(envioService.verEnvio(id));
    }

    //Ver Todos los pedidos pendientes
    @GetMapping("/ver-pendientes")
    public ResponseEntity<List<EnvioDto>> verPendientes(){
         return ResponseEntity.ok(envioService.verEnviosPendientes());
    }

    //Ver todos los envios creados del usuario
    @GetMapping("/mis-envios")
    public ResponseEntity<List<MiEnvioDto>> verMisEnvios(){
        return ResponseEntity.ok(envioService.verMisEnvios());
    }


    @PostMapping
    public ResponseEntity<Map<String, String>> crear(@RequestBody @Valid CrearEnvioDto dto) {
        envioService.crearEnvio(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "¡Envío publicado con éxito!"));
    }

    @PostMapping("/{id}/tomar")
    public ResponseEntity<EnvioDto> tomarEnvio(@PathVariable Long id) {
        return ResponseEntity.ok(envioService.tomarEnvio(id));
    }

    @PostMapping("/{id}/cerrar")
    public ResponseEntity<EnvioDto> cerrarEnvio(@PathVariable Long id, @RequestBody String codigoConfirmacion) {
           return ResponseEntity.ok(envioService.cerrarEnvio(id, codigoConfirmacion));
    }
}