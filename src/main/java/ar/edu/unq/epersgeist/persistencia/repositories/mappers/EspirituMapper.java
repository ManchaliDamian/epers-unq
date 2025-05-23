package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituAngelicalJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituDemoniacoJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UbicacionMapper.class})
public interface EspirituMapper {
    @Mapping(target = "mediumConectado.espiritus", ignore = true)
    EspirituAngelical toDomainAngel(EspirituAngelicalJPADTO jpa);

    @Mapping(target = "mediumConectado.espiritus", ignore = true)
    EspirituDemoniaco toDomainDemonio(EspirituDemoniacoJPADTO jpa);

    default Espiritu toDomain(EspirituJPADTO jpa){
        return switch (jpa.getTipo()){
            case ANGELICAL -> toDomainAngel((EspirituAngelicalJPADTO) jpa);
            case DEMONIACO -> toDomainDemonio((EspirituDemoniacoJPADTO) jpa);
           };
    }

    default EspirituJPADTO toJpa(Espiritu espiritu){
        return switch (espiritu.getTipo()){
            case ANGELICAL -> toJpaAngel((EspirituAngelical)espiritu);
            case DEMONIACO -> toJpaDemonio((EspirituDemoniaco)espiritu);
        };
    }


    @Mapping(target = "mediumConectado.id", source = "mediumConectado.id")
    @Mapping(target = "mediumConectado.espiritus", ignore = true)
    EspirituAngelicalJPADTO toJpaAngel(EspirituAngelical espiritu);


    @Mapping(target = "mediumConectado.id", source = "mediumConectado.id")
    @Mapping(target = "mediumConectado.espiritus", ignore = true)
    EspirituDemoniacoJPADTO toJpaDemonio(EspirituDemoniaco espiritu);

    default List<EspirituJPADTO> toJPAList(List<Espiritu> espiritus) {
        return espiritus.stream().map(this::toJpa).collect(Collectors.toList());
    }

    List<Espiritu> toDomainList(List<EspirituJPADTO> espirituJPADTOS);
    List<EspirituDemoniaco> toDomainListDemoniaco(List<EspirituDemoniacoJPADTO> espirituDemoniacoJPADTOS);
    List<EspirituAngelical> toDomainListAngelical(List<EspirituAngelicalJPADTO> espirituAngelicalJPADTOS);

    default EspirituJPADTO actualizarJpaCon(EspirituJPADTO espirituJPADTO, Espiritu espiritu){
        return switch (espiritu.getTipo()){
            case ANGELICAL -> actualizarEspirituAngelicalJpaCon((EspirituAngelicalJPADTO) espirituJPADTO, (EspirituAngelical) espiritu);
            case DEMONIACO -> actualizarEspirituDemoniacoJpaCon((EspirituDemoniacoJPADTO) espirituJPADTO, (EspirituDemoniaco) espiritu);
        };
    }

    @Mapping(target = "id", ignore = true)
    EspirituAngelicalJPADTO actualizarEspirituAngelicalJpaCon(
            @MappingTarget EspirituAngelicalJPADTO espirituJPADTO,
            EspirituAngelical espiritu
    );

    @Mapping(target = "id", ignore = true)
    EspirituDemoniacoJPADTO actualizarEspirituDemoniacoJpaCon(
            @MappingTarget EspirituDemoniacoJPADTO espirituJPADTO,
            EspirituDemoniaco espiritu
    );

}
