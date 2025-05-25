package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.CementerioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.SantuarioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;

import java.util.List;


public interface UbicacionMapper {

    // toDomain
    Santuario toDomainSantuario(SantuarioJPADTO jpa);
    Cementerio toDomainCementerio(CementerioJPADTO jpa);
    Ubicacion toDomain(UbicacionJPADTO jpa); // Ahora es abstracto

    List<Ubicacion> toDomainList(List<UbicacionJPADTO> ubicacionJPADTOS);
    List<Cementerio> toDomainListCementerio(List<CementerioJPADTO> cementerios);
    List<Santuario> toDomainListSantuarios(List<SantuarioJPADTO> santuarios);

    // toJpa
    UbicacionJPADTO toJpa(Ubicacion ubicacion); // Ahora es abstracto
    SantuarioJPADTO toJpa(Santuario santuario);
    CementerioJPADTO toJpa(Cementerio cementerio);

    SantuarioJPADTO actualizarJpaCon(SantuarioJPADTO ubiJPA, Santuario ubicacion);
    CementerioJPADTO actualizarJpaCon(CementerioJPADTO ubiJPA, Cementerio ubicacion);

    UbicacionJPADTO actualizarJPA(UbicacionJPADTO ubiJPA, Ubicacion ubicacion);

    // toNeo
    UbicacionNeoDTO toNeo(Ubicacion ubicacion);
    Ubicacion fromNeo(UbicacionNeoDTO neo);

}

