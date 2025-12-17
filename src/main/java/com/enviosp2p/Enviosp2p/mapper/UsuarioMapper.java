package com.enviosp2p.Enviosp2p.mapper;

import com.enviosp2p.Enviosp2p.dto.RegistroRequestDto;
import com.enviosp2p.Enviosp2p.entity.Usuario;
import com.enviosp2p.Enviosp2p.enums.Rol;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component // Lo marcamos como componente para poder inyectarlo
public class UsuarioMapper {

    // Metodo que convierte el DTO a Entidad
    // Recibimos la password ya encriptada para no meter lógica de seguridad aquí
    public Usuario toEntity(RegistroRequestDto dto, String passwordEncriptada) {
        return Usuario.builder()
                .nombre(dto.nombre())
                .apellido(dto.apellido())
                .correo(dto.correo())
                .contrasena(passwordEncriptada) // Usamos la encriptada
                .rol(Rol.USER) // Rol por defecto
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }
}