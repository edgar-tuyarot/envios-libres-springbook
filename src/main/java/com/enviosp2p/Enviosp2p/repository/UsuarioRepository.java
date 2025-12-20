package com.enviosp2p.Enviosp2p.repository;

import com.enviosp2p.Enviosp2p.entity.Usuario;
import com.enviosp2p.Enviosp2p.exceptions.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Buscar por correo.
    Optional<Usuario> findByCorreo(String correo);

    //buscamos el usuario por su token de recuperación
    Optional<Usuario> findByTokenRecuperacion(String tokenRecuperacion);

    //Validar si existe el usuario por el correo
    boolean existsByCorreo(String correo);

    //Buscar por el token de activacion
    Optional<Usuario> findByTokenActivacion(String token);

}
