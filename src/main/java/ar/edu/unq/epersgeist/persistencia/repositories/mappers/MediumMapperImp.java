package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumJPADTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("mediumMapperImpl")
public class MediumMapperImp  implements MediumMapper{

    private final UbicacionMapper ubicacionMapper;
    private final @Lazy EspirituMapper espirituMapper;

    public MediumMapperImp(UbicacionMapper ubicacionMapper, @Lazy EspirituMapper espirituMapper) {
        this.ubicacionMapper = ubicacionMapper;
        this.espirituMapper = espirituMapper;
    }
    @Override
    public MediumJPADTO toJpa(Medium medium) {
        if (medium == null) { // <-- ¡Falta esta verificación!
            return null;
        }
        MediumJPADTO mediumJPA = new MediumJPADTO(medium.getNombre(), medium.getManaMax(),medium.getMana(), ubicacionMapper.toJpa(medium.getUbicacion()) );
        mediumJPA.setId(medium.getId());
        mediumJPA.setDeleted(medium.isDeleted());
        mediumJPA.setCreatedAt(medium.getCreatedAt());
        mediumJPA.setUpdatedAt(medium.getUpdatedAt());
        List<Espiritu> espiritusList =
                medium.getEspiritus().stream().collect(Collectors.toList());
        List<EspirituJPADTO> espiritusJPAList = espirituMapper.toJPAList(espiritusList);
        mediumJPA.getEspiritus().addAll(espiritusJPAList);
        return mediumJPA;
    }

    @Override
    public Medium toDomain(MediumJPADTO mediumJPADTO) {
        Medium medium = new Medium(
                                    mediumJPADTO.getNombre(),
                mediumJPADTO.getManaMax(), mediumJPADTO.getMana(), ubicacionMapper.toDomain(mediumJPADTO.getUbicacion()));
        medium.setId(mediumJPADTO.getId());
        medium.setDeleted(mediumJPADTO.isDeleted());
        medium.setCreatedAt(mediumJPADTO.getCreatedAt());
        medium.setUpdatedAt(mediumJPADTO.getUpdatedAt());
        List<EspirituJPADTO> espiritusJPAList =
                mediumJPADTO.getEspiritus().stream().collect(Collectors.toList());
        List<Espiritu> espiritusList = espirituMapper.toDomainList(espiritusJPAList);
        medium.getEspiritus().addAll(espiritusList);
        return medium;
    }

    @Override
    public List<Medium> toDomainList(List<MediumJPADTO> mediumList) {
        if (mediumList == null) { // Manejar el caso de lista nula
            return new ArrayList<>(); // O Collections.emptyList()
        }
        return mediumList.stream()
                .map(this::toDomain) // Reutiliza tu método toDomain individual
                .collect(Collectors.toList());
    }
}
