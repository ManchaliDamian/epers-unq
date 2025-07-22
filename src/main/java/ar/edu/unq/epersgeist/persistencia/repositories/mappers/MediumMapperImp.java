package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumMongoDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component("mediumMapperImpl")
public class MediumMapperImp implements MediumMapper {

    private final UbicacionMapper ubicacionMapper;
    private final EspirituMapper espirituMapper;

    public MediumMapperImp(UbicacionMapper ubicacionMapper, @Lazy EspirituMapper espirituMapper) {
        this.ubicacionMapper = ubicacionMapper;
        this.espirituMapper = espirituMapper;
    }

    @Override
    public MediumJPADTO toJpa(Medium medium) {
        return toJpa(medium, new IdentityHashMap<>());
    }

    @Override
    public Medium toDomain(MediumJPADTO mediumJPADTO) {
        return toDomain(mediumJPADTO, new IdentityHashMap<>());
    }

    @Override
    public MediumJPADTO toJpa(Medium medium, Map<Object, Object> context) {
        if (medium == null) return null;
        if (context.containsKey(medium)) return (MediumJPADTO) context.get(medium);

        MediumJPADTO mediumJPA = new MediumJPADTO(medium.getNombre(), medium.getManaMax(), medium.getMana(), ubicacionMapper.toJpa(medium.getUbicacion()));
        context.put(medium, mediumJPA);

        mediumJPA.setId(medium.getId());
        mediumJPA.setDeleted(medium.isDeleted());
        mediumJPA.setCreatedAt(medium.getCreatedAt());
        mediumJPA.setUpdatedAt(medium.getUpdatedAt());

        if (medium.getEspiritus() != null) {

            List<EspirituJPADTO> espiritusJPAList = espirituMapper.toJPAList(
                    new ArrayList<>(medium.getEspiritus()),
                    context
            );
            mediumJPA.getEspiritus().addAll(espiritusJPAList);
        }
        return mediumJPA;
    }

    @Override
    public Medium toDomain(MediumJPADTO mediumJPADTO, Map<Object, Object> context) {
        if (mediumJPADTO == null) return null;
        if (context.containsKey(mediumJPADTO)) return (Medium) context.get(mediumJPADTO);

        Medium medium = new Medium(
                mediumJPADTO.getNombre(),
                mediumJPADTO.getManaMax(), mediumJPADTO.getMana(),
                ubicacionMapper.toDomain(mediumJPADTO.getUbicacion()));
        context.put(mediumJPADTO, medium);

        medium.setId(mediumJPADTO.getId());
        medium.setDeleted(mediumJPADTO.isDeleted());
        medium.setCreatedAt(mediumJPADTO.getCreatedAt());
        medium.setUpdatedAt(mediumJPADTO.getUpdatedAt());

        if (mediumJPADTO.getEspiritus() != null) {
            List<Espiritu> espiritusList = espirituMapper.toDomainList(
                    new ArrayList<>(mediumJPADTO.getEspiritus()),
                    context
            );
            medium.getEspiritus().addAll(espiritusList);
        }
        return medium;
    }

    @Override
    public List<Medium> toDomainList(List<MediumJPADTO> mediumList) {
        if (mediumList == null) {
            return new ArrayList<>();
        }
        return mediumList.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public MediumMongoDTO toMongo(MediumJPADTO jpa, Coordenada coordenada) {
        GeoJsonPoint punto = new GeoJsonPoint(coordenada.getLongitud(), coordenada.getLatitud());
        MediumMongoDTO dto = new MediumMongoDTO(punto);
        dto.setIdSQL(jpa.getId());
        return dto;
    }
}