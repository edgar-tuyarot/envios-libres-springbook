package com.enviosp2p.Enviosp2p.service;

import com.enviosp2p.Enviosp2p.entity.Usuario;
import com.enviosp2p.Enviosp2p.exceptions.UserNotFoundException;
import com.enviosp2p.Enviosp2p.repository.UsuarioRepository;

public class UsuarioService {

    UsuarioRepository usuarioRepository;

    // Metodo para buscar perfil (Aquí sí usas la tuya para que el GlobalHandler responda bonito)
    public Usuario obtenerDatosDePerfil(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("El usuario con id " + id + " no existe"));
    }

}
