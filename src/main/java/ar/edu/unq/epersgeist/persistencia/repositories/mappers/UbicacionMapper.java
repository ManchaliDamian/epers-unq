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
    @Mapping(source = "createdAt", target = "createdAt")
    Santuario toDomainSantuario(SantuarioJPADTO jpa);
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "createdAt", target = "createdAt")
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
    default Ubicacion actualizarModelo(Ubicacion ubi, UbicacionJPADTO ubiJPA){
        return switch (ubiJPA){
            case CementerioJPADTO c -> actualizarModeloCon((Cementerio) ubi, c);
            case SantuarioJPADTO s -> actualizarModeloCon((Santuario) ubi, s);
            default -> throw new IllegalStateException("Unexpected value: " + ubiJPA);
        };
    }

    @Mapping(target = "updatedAt", ignore = true)
    Santuario actualizarModeloCon(
            @MappingTarget Santuario santuario,
            SantuarioJPADTO santuarioJPADTO
    );

    @Mapping(target = "updatedAt", ignore = true)
    Cementerio actualizarModeloCon(
            @MappingTarget Cementerio cementerio,
            CementerioJPADTO cementerioJPADTO
    );

    @Mapping(target = "updatedAt", ignore = true)
    SantuarioJPADTO actualizarJpaCon(
            @MappingTarget SantuarioJPADTO santuarioJPADTO,
            Santuario santuario
    );


    @Mapping(target = "updatedAt", ignore = true)
    CementerioJPADTO actualizarJpaCon(
            @MappingTarget CementerioJPADTO cementerioJPADTO,
            Cementerio cementerio
    );

    //toNeo
    UbicacionNeoDTO toNeo(Ubicacion ubicacion);


}

