package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.CementerioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.SantuarioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UbicacionMapper {

    //toDomain
    Santuario toDomainSantuario(SantuarioJPADTO jpa);
    Cementerio toDomainCementerio(CementerioJPADTO jpa);
    default Ubicacion toDomain(UbicacionJPADTO jpa){
        return switch (jpa.getTipo()){
            case CEMENTERIO -> toDomainCementerio((CementerioJPADTO) jpa);
            case SANTUARIO -> toDomainSantuario((SantuarioJPADTO) jpa);
        };
    }
    List<Ubicacion> toDomainList(List<UbicacionJPADTO> ubicacionJPADTOS);
    List<Cementerio> toDomainListCementerio(List<CementerioJPADTO> cementerios);
    List<Santuario> toDomainListSantuarios(List<SantuarioJPADTO> santuarios);

    //toJpa
    default UbicacionJPADTO toJpa(Ubicacion ubicacion){
        return switch (ubicacion){
            case Cementerio c -> toJpa(c);
            case Santuario s -> toJpa(s);
            default -> throw new IllegalStateException("Unexpected value: " + ubicacion);
        };
    }

    SantuarioJPADTO toJpa(Santuario santuario);
    CementerioJPADTO toJpa(Cementerio cementerio);
    // Actualiza un UbicacionJPA desde el modelo de dominio

    default UbicacionJPADTO actualizarJpaCon(UbicacionJPADTO ubiJPA, Ubicacion ubicacion){
        return switch (ubicacion){
            case Cementerio c -> actualizarJpaCon(ubiJPA, c);
            case Santuario s -> actualizarJpaCon(ubiJPA, s);
            default -> throw new IllegalStateException("Unexpected value: " + ubicacion);
        };
    }

    //toNeo
    UbicacionNeoDTO toNeo(Ubicacion ubicacion);


}

