package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.CementerioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.SantuarioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UbicacionMapper {

    //toDomain
    @Mapping(source = "updatedAt", target = "updatedAt")
    Santuario toDomainSantuario(SantuarioJPADTO jpa);
    @Mapping(source = "updatedAt", target = "updatedAt")
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

    default UbicacionJPADTO actualizarJpa(UbicacionJPADTO ubiJPA, Ubicacion ubicacion){
        return switch (ubicacion){
            case Cementerio c -> actualizarJpaCon((CementerioJPADTO) ubiJPA, c);
            case Santuario s -> actualizarJpaCon((SantuarioJPADTO) ubiJPA, s);
            default -> throw new IllegalStateException("Unexpected value: " + ubicacion);
        };
    }
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SantuarioJPADTO actualizarJpaCon(
            @MappingTarget SantuarioJPADTO santuarioJPADTO,
            Santuario santuario
    );

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CementerioJPADTO actualizarJpaCon(
            @MappingTarget CementerioJPADTO cementerioJPADTO,
            Cementerio cementerio
    );

    //toNeo
    UbicacionNeoDTO toNeo(Ubicacion ubicacion);


}

