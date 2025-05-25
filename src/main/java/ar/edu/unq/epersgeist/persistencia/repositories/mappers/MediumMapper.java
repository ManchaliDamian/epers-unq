package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumJPADTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;


public interface MediumMapper {

    MediumJPADTO toJpa(Medium medium);

    Medium toDomain(MediumJPADTO mediumJPADTO);

    // --- Métodos internos con contexto ---
    MediumJPADTO toJpa(Medium medium, Map<Object, Object> context);

    Medium toDomain(MediumJPADTO mediumJPADTO, Map<Object, Object> context);

    List<Medium> toDomainList(List<MediumJPADTO> mediumList);
}
