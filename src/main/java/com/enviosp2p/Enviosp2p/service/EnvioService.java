package com.enviosp2p.Enviosp2p.service;

import com.enviosp2p.Enviosp2p.dto.envios.CrearEnvioDto;
import com.enviosp2p.Enviosp2p.dto.envios.EnvioDto;
import com.enviosp2p.Enviosp2p.dto.envios.MiEnvioDto;
import com.enviosp2p.Enviosp2p.entity.Envio;
import com.enviosp2p.Enviosp2p.entity.Usuario;
import com.enviosp2p.Enviosp2p.enums.EstadoEnvio;
import com.enviosp2p.Enviosp2p.exceptions.BusinessException;
import com.enviosp2p.Enviosp2p.exceptions.ResourceNotFoundException;
import com.enviosp2p.Enviosp2p.exceptions.UserNotFoundException;
import com.enviosp2p.Enviosp2p.mapper.EnvioMapper;
import com.enviosp2p.Enviosp2p.repository.EnvioRepository;
import com.enviosp2p.Enviosp2p.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EnvioMapper envioMapper;

    public EnvioDto verEnvio(Long id){
        return envioRepository.findById(id)
                .map(envioMapper::aDto)
                .orElseThrow(() -> new ResourceNotFoundException("Envio no encontrado"));
    }

    @Transactional // Importante: Si algo falla, hace rollback de todo
    public void crearEnvio(CrearEnvioDto dto) {

        //OBTENER EL USUARIO LOGUEADO (El Remitente)
        String emailUsuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario remitente = usuarioRepository.findByCorreo(emailUsuarioActual)
                .orElseThrow(() -> new UserNotFoundException("Error de integridad: Usuario del token no encontrado en BD"));

        //CONVERTIR DTO A ENTIDAD (Usando tu Mapper y el usuario real)
        Envio nuevoEnvio = envioMapper.aEntidad(dto, remitente);

        //GUARDAR
        envioRepository.save(nuevoEnvio);
    }

    @Transactional
    public EnvioDto tomarEnvio(Long envioId) {

        //Obtener al Viajero (Usuario logueado)
        String emailViajero = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario viajero = usuarioRepository.findByCorreo(emailViajero)
                .orElseThrow(() -> new UserNotFoundException("Usuario viajero no encontrado"));

        //Obtener el Envío
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new ResourceNotFoundException("Envío inexistente"));


        //El estado debe ser PENDIENTE
        if (envio.getEstado() != EstadoEnvio.PENDIENTE) {
            throw new BusinessException("El envío ya no está disponible. Estado actual: " + envio.getEstado());
        }

        //El viajero no puede ser el mismo remitente
        if (envio.getRemitente().getId().equals(viajero.getId())) {
            throw new BusinessException("No puedes tomar tu propio envío.");
        }
        envio.setViajero(viajero);
        envio.setEstado(EstadoEnvio.ASIGNADO);
        envio.setFechaAsignacion(LocalDateTime.now());

        envioRepository.save(envio);

        return envioMapper.aDto(envio);

    }

    //Buscar mi pedido asignado
    public List<EnvioDto> verMiEnvioAsignado(){
        List<EnvioDto> enviosDto =  new ArrayList<>();
        String emailUsuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario remitente = usuarioRepository.findByCorreo(emailUsuarioActual)
                .orElseThrow(() -> new UserNotFoundException("Error de integridad: Usuario del token no encontrado en BD"));
        List<Envio> enviosAsignados = envioRepository.findByViajero(remitente);


        for (Envio envio : enviosAsignados){
            if (envio.getEstado()==EstadoEnvio.ASIGNADO || envio.getEstado()==EstadoEnvio.EN_TRANSITO){
                enviosDto.add(envioMapper.aDto(envio));
            }
        }

        return enviosDto;

    }

    //Envios disponibles para tomar
    public List<EnvioDto> verEnviosPendientes() {
        List<Envio> enviosPendientes = envioRepository.findByEstado(EstadoEnvio.PENDIENTE);
        return enviosPendientes.stream()
                .map(envioMapper::aDto)
                .toList();

    }

    //Envios disponibles para tomar
    public List<EnvioDto> verMiEnviosCerrados(){
        List<EnvioDto> enviosDto =  new ArrayList<>();
        String emailUsuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario remitente = usuarioRepository.findByCorreo(emailUsuarioActual)
                .orElseThrow(() -> new UserNotFoundException("Error de integridad: Usuario del token no encontrado en BD"));
        List<Envio> enviosAsignados = envioRepository.findByViajeroAndEstado(remitente, EstadoEnvio.ENTREGADO);


        for (Envio envio : enviosAsignados){
            enviosDto.add(envioMapper.aDto(envio));
        }

        return enviosDto;

    }

    //Soltar Pedido
    public void liberarEnvio(Long id){
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envío inexistente"));

        if (envio.getEstado()==EstadoEnvio.ASIGNADO){
            envio.setEstado(EstadoEnvio.PENDIENTE);
            envio.setViajero(null);
            envioRepository.save(envio);
        }

    }

    //Comenzar viaje
    @Transactional
    public EnvioDto comenzarViaje(Long envioId) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new ResourceNotFoundException("Envío inexistente"));

        if (envio.getEstado() != EstadoEnvio.ASIGNADO) {
            throw new BusinessException("El envío no está asignado a ningún viajero.");
        }
        String emailUsuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!envio.getViajero().getCorreo().equals(emailUsuarioActual)){
            throw new BusinessException("No eres el remitente del envío.");
        }
        envio.setEstado(EstadoEnvio.EN_TRANSITO);
        envio.setFechaCierre(LocalDateTime.now());
        envioRepository.save(envio);

        return envioMapper.aDto(envio);
    }

    //Envios creados por el usuario
    public List<MiEnvioDto> verMisEnvios() {
        String emailUsuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario remitente = usuarioRepository.findByCorreo(emailUsuarioActual)
                .orElseThrow(() -> new UserNotFoundException("Error de integridad: Usuario del token no encontrado en BD"));

        List<Envio> enviosPendientes = envioRepository.findByRemitente(remitente);

        return enviosPendientes.stream()
                .map(envioMapper::MiEnvioaDto)
                .toList();

    }
    //Cerrar envio
    @Transactional
    public EnvioDto cerrarEnvio(Long envioId, String codigoConfirmacion) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new ResourceNotFoundException("Envío inexistente"));

        if (envio.getEstado() != EstadoEnvio.EN_TRANSITO) {
            throw new BusinessException("El envío no está en transitoa.");
        }
        if (envio.getCodigo_confirmacion() != null || envio.getCodigo_confirmacion().equals(codigoConfirmacion)){
            envio.setEstado(EstadoEnvio.ENTREGADO);
            envio.setFechaCierre(LocalDateTime.now());
            envioRepository.save(envio);

            return envioMapper.aDto(envio);

        }
        return envioMapper.aDto(envio);
    }


    //Comenzar viaje
    @Transactional
    public EnvioDto cancelaEnvio(Long envioId) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new ResourceNotFoundException("Envío inexistente"));

        if (envio.getEstado() != EstadoEnvio.ASIGNADO) {
            throw new BusinessException("El envío no está asignado a ningún viajero.");
        }
        String emailUsuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!envio.getRemitente().getCorreo().equals(emailUsuarioActual)){
            throw new BusinessException("No eres el remitente del envío.");
        }
        envio.setEstado(EstadoEnvio.CANCELADO);
        envio.setFechaCierre(LocalDateTime.now());
        envioRepository.save(envio);

        return envioMapper.aDto(envio);
    }

}