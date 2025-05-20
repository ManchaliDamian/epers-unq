package ar.edu.unq.epersgeist.mapper;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.persistencia.EspirituJPA.EspirituAngelicalJPA;
import ar.edu.unq.epersgeist.persistencia.EspirituJPA.EspirituDemoniacoJPA;
import ar.edu.unq.epersgeist.persistencia.EspirituJPA.EspirituJPA;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring", uses = UbicacionMapper.class)
public interface EspirituMapper {

    EspirituAngelical toModel(EspirituAngelicalJPA jpa);
    EspirituDemoniaco toModel(EspirituDemoniacoJPA jpa);

    default Espiritu toModel(EspirituJPA jpa) {
        return switch (jpa.getTipo()) {
            case ANGELICAL -> toModel((EspirituAngelicalJPA) jpa);
            case DEMONIACO -> toModel((EspirituDemoniacoJPA) jpa);
        };
    }


    EspirituDemoniacoJPA toJPA(EspirituDemoniaco demon);
    EspirituAngelicalJPA toJPA(EspirituAngelical angel);

    default EspirituJPA toJPA(Espiritu e) {
        return switch (e.getTipo()) {
            case DEMONIACO -> toJPA((EspirituDemoniaco) e);
            case ANGELICAL -> toJPA((EspirituAngelical) e);
        };
    }
    default List<EspirituJPA> toJPAList(List<Espiritu> espiritus) {
        return espiritus.stream().map(this::toJPA).collect(Collectors.toList());
    }



    default Optional<Espiritu> toModel(Optional<EspirituJPA> jpa) {
        return jpa.map(this::toModel);
    }


    default List<Espiritu> toModelList(List<EspirituJPA> jpas) {
        return jpas.stream().map(this::toModel).collect(Collectors.toList());
    }
    default List<EspirituAngelical> toModelListAngeles(List<EspirituAngelicalJPA> jpas) {
        return jpas.stream().map(this::toModel).collect(Collectors.toList());
    }
    default List<EspirituDemoniaco> toModelListDemoniaco(List<EspirituDemoniacoJPA> jpas) {
        return jpas.stream().map(this::toModel).collect(Collectors.toList());
    }
}
