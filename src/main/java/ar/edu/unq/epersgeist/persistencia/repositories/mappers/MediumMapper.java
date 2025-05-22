package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumJPADTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EspirituMapper.class, UbicacionMapper.class})
public interface MediumMapper {
    @Mapping(target = "espiritus", ignore = true)
    MediumJPADTO toJpa(Medium medium);
    @Mapping(target = "espiritus", ignore = true)
    Medium toDomain(MediumJPADTO mediumJPADTO);
    @Mapping(target = "espiritus", ignore = true)
    List<Medium> toDomainList(List<MediumJPADTO> mediumList);
}
