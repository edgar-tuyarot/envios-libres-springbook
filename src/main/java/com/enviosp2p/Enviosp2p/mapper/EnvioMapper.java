package com.enviosp2p.Enviosp2p.mapper;

import com.enviosp2p.Enviosp2p.dto.envios.CrearEnvioDto;
import com.enviosp2p.Enviosp2p.dto.envios.DireccionDto;
import com.enviosp2p.Enviosp2p.dto.envios.EnvioDto;
import com.enviosp2p.Enviosp2p.dto.envios.MiEnvioDto;
import com.enviosp2p.Enviosp2p.dto.usuarios.UsuarioResponseDto;
import com.enviosp2p.Enviosp2p.entity.Direccion;
import com.enviosp2p.Enviosp2p.entity.Envio;
import com.enviosp2p.Enviosp2p.entity.Usuario;
import com.enviosp2p.Enviosp2p.utils.GeneradorCodigo;
import org.springframework.stereotype.Component;

@Component
public class EnvioMapper {

    // Convierte el DTO que llega del Front a la Entidad que guardamos en BD
    public Envio aEntidad(CrearEnvioDto dto, Usuario remitente) {
        Envio envio = new Envio();
        envio.setDescripcion(dto.descripcion());
        envio.setRecompensa(dto.recompensa());
        envio.setRemitente(remitente);
        envio.setCelular(dto.celular());

        //Mapeo manual de las direcciones (Embeddables)
        envio.setOrigen(mapearDireccion(dto.origen()));
        envio.setDestino(mapearDireccion(dto.destino()));

        //Distancia
        envio.setDistancia(dto.distancia());

        //Codigo de confirmacion para cerrar envio
        envio.setCodigo_confirmacion(GeneradorCodigo.generar(6));

        // El estado y fecha se setean solos en el @PrePersist de la entidad
        return envio;
    }

    // Metodo auxiliar privado para no repetir código
    private Direccion mapearDireccion(DireccionDto dto) {
        if (dto == null) return null;
        return new Direccion(
                dto.calle(),
                dto.altura(),
                dto.ciudad(),
                dto.provincia(),
                dto.pais(),
                dto.latitud(),
                dto.longitud()
        );
    }


    public EnvioDto aDto(Envio entidad) {
        if (entidad == null) return null;

        return new EnvioDto(
                //Id
                entidad.getId(),
                //Remitente
                mapUsuarioToDto(entidad.getRemitente()),
                //Celular
                entidad.getCelular(),
                //Descripcion
                entidad.getDescripcion(),
                //Origen
                mapDireccionToDto(entidad.getOrigen()),
                //Destino
                mapDireccionToDto(entidad.getDestino()),
                //Estado
                entidad.getEstado(),
                //Recompensa
                entidad.getRecompensa(),
                //Distancia
                entidad.getDistancia(),
                //Fecha Creacion
                entidad.getFechaCreacion()



        );
    }

    //Envio del usuaro a Dto (Incluye codigo de cofirmacion)
    public MiEnvioDto MiEnvioaDto(Envio entidad) {
        if (entidad == null) return null;

        return new MiEnvioDto(
                //Id
                entidad.getId(),
                //Remitente
                mapUsuarioToDto(entidad.getRemitente()),
                //Viajero
                mapUsuarioToDto(entidad.getViajero()),
                //Descripcion
                entidad.getDescripcion(),
                //Origen
                mapDireccionToDto(entidad.getOrigen()),
                //Destino
                mapDireccionToDto(entidad.getDestino()),
                //Estado
                entidad.getEstado(),
                //Codigo Confirmacion
                entidad.getCodigo_confirmacion(),
                //Recompensa
                entidad.getRecompensa(),
                //Distancia
                entidad.getDistancia(),
                //Fecha Creacion
                entidad.getFechaCreacion(),
                //Fecha Asignacion
                entidad.getFechaAsignacion(),
                //Fecha Cierre
                entidad.getFechaCierre()

        );
    }


    //Usuario a Dto
    private UsuarioResponseDto mapUsuarioToDto(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNombre(), // Asumiendo que agregaste nombre a Usuario, sino usa email
                usuario.getCorreo()
        );
    }

    //Direcion a Dto
    private DireccionDto mapDireccionToDto(Direccion dir) {
        if (dir == null) return null;
        return new DireccionDto(
                dir.getCalle(),
                dir.getAltura(),
                dir.getCiudad(),
                dir.getProvincia(),
                dir.getPais(),
                dir.getLatitud(),
                dir.getLongitud()
        );
    }




}