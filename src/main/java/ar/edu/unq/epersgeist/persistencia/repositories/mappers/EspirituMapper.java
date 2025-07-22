package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituAngelicalJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituDemoniacoJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public interface EspirituMapper {
    //toDomain
    Espiritu toDomain(EspirituJPADTO jpa, Map<Object, Object> context);

    EspirituAngelical toDomainAngel(EspirituAngelicalJPADTO jpa);
    EspirituDemoniaco toDomainDemonio(EspirituDemoniacoJPADTO jpa);

    default Espiritu toDomain(EspirituJPADTO jpa){
        return switch (jpa.getTipo()){
            case ANGELICAL -> toDomainAngel((EspirituAngelicalJPADTO) jpa);
            case DEMONIACO -> toDomainDemonio((EspirituDemoniacoJPADTO) jpa);
        };
    }

    //toJpa
    default EspirituJPADTO toJpa(Espiritu espiritu){
        return switch (espiritu.getTipo()){
            case ANGELICAL -> toJpaAngel((EspirituAngelical)espiritu);
            case DEMONIACO -> toJpaDemonio((EspirituDemoniaco)espiritu);
        };
    }

    EspirituJPADTO toJpa(Espiritu espiritu, Map<Object, Object> context);


    EspirituAngelicalJPADTO toJpaAngel(EspirituAngelical espiritu);



    EspirituDemoniacoJPADTO toJpaDemonio(EspirituDemoniaco espiritu);

    default List<EspirituJPADTO> toJPAList(List<Espiritu> espiritus) {
        return espiritus.stream().map(this::toJpa).collect(Collectors.toList());
    }

    List<Espiritu> toDomainList(List<EspirituJPADTO> espirituJPADTOS);


    EspirituDemoniacoJPADTO toJpaDemonio(EspirituDemoniaco espiritu, Map<Object, Object> context);

    List<Espiritu> toDomainList(List<EspirituJPADTO> espirituJPADTOS, Map<Object, Object> context);

    List<EspirituJPADTO> toJPAList(List<Espiritu> espiritus, Map<Object, Object> context);

    List<EspirituDemoniaco> toDomainListDemoniaco(List<EspirituDemoniacoJPADTO> espirituDemoniacoJPADTOS);
    List<EspirituAngelical> toDomainListAngelical(List<EspirituAngelicalJPADTO> espirituAngelicalJPADTOS);

    default EspirituJPADTO actualizarJpaCon(EspirituJPADTO espirituJPADTO, Espiritu espiritu){
        return switch (espiritu.getTipo()){
            case ANGELICAL -> actualizarEspirituAngelicalJpaCon((EspirituAngelicalJPADTO) espirituJPADTO, (EspirituAngelical) espiritu);
            case DEMONIACO -> actualizarEspirituDemoniacoJpaCon((EspirituDemoniacoJPADTO) espirituJPADTO, (EspirituDemoniaco) espiritu);
        };
    }


    EspirituAngelicalJPADTO actualizarEspirituAngelicalJpaCon(
            EspirituAngelicalJPADTO espirituJPADTO,
            EspirituAngelical espiritu
    );


    EspirituDemoniacoJPADTO actualizarEspirituDemoniacoJpaCon(
            EspirituDemoniacoJPADTO espirituJPADTO,
            EspirituDemoniaco espiritu
    );


    //toMongo
    EspirituMongoDTO toMongo(EspirituJPADTO jpa, Coordenada coordenada);
    Coordenada toCoordenada(EspirituMongoDTO mongo);

    Espiritu addFirestoreData(Espiritu sinFirestoreData, Espiritu conFirestoreData);
}
