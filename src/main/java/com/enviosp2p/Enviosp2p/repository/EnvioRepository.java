package com.enviosp2p.Enviosp2p.repository;

import com.enviosp2p.Enviosp2p.entity.Envio;
import com.enviosp2p.Enviosp2p.entity.Usuario;
import com.enviosp2p.Enviosp2p.enums.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnvioRepository extends JpaRepository<Envio, Long> {


    Envio findByRemitenteAndEstado(Usuario remitente, EstadoEnvio estadoEnvio);

    List<Envio> findByViajeroAndEstado(Usuario viajero, EstadoEnvio estadoEnvio);

    List<Envio> findByEstado(EstadoEnvio estadoEnvio);

    List<Envio> findByRemitente(Usuario remitente);

}
