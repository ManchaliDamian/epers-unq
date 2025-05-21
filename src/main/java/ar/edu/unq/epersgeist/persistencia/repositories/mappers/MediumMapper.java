package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumJPADTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


public interface MediumMapper {

    MediumJPADTO toJpa(Medium medium);

    Medium toDomain(MediumJPADTO mediumJPADTO);

    List<Medium> toDomainList(List<MediumJPADTO> mediumList);
}
