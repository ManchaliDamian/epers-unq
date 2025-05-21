package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituAngelicalJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituDemoniacoJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component("espirituMapperImpl")
public class EspirituMapperImp implements EspirituMapper{
    private final UbicacionMapper ubicacionMapper;
    private final  MediumMapper mediumMapper;

    public EspirituMapperImp(UbicacionMapper ubicacionMapper, MediumMapper mediumMapper) {
        this.ubicacionMapper = ubicacionMapper;
        this.mediumMapper = mediumMapper;
    }

    @Override
    public EspirituAngelical toDomainAngel(EspirituAngelicalJPADTO jpa) {
        EspirituAngelical espiritu = new EspirituAngelical(jpa.getNombre(),ubicacionMapper.toDomain(jpa.getUbicacion()));
        espiritu.setId(jpa.getId());
        espiritu.setDeleted(jpa.isDeleted());
        espiritu.setCreatedAt(jpa.getCreatedAt());
        espiritu.setUpdatedAt(jpa.getUpdatedAt());
        espiritu.setMediumConectado(mediumMapper.toDomain(jpa.getMediumConectado()));
        return espiritu;
    }

    @Override
    public EspirituDemoniaco toDomainDemonio(EspirituDemoniacoJPADTO jpa) {
        EspirituDemoniaco espiritu = new EspirituDemoniaco(jpa.getNombre(), ubicacionMapper.toDomain(jpa.getUbicacion()));
        espiritu.setId(jpa.getId());
        espiritu.setDeleted(jpa.isDeleted());
        espiritu.setCreatedAt(jpa.getCreatedAt());
        espiritu.setUpdatedAt(jpa.getUpdatedAt());
        espiritu.setMediumConectado(mediumMapper.toDomain(jpa.getMediumConectado()));
        return espiritu;
    }

    @Override
    public EspirituAngelicalJPADTO toJpaAngel(EspirituAngelical espiritu) {
        EspirituAngelicalJPADTO espirituJPA = new EspirituAngelicalJPADTO(espiritu.getNombre(), ubicacionMapper.toJpa(espiritu.getUbicacion()));
        espirituJPA.setId(espiritu.getId());
        espirituJPA.setDeleted(espiritu.isDeleted());
        espirituJPA.setCreatedAt(espiritu.getCreatedAt());
        espirituJPA.setUpdatedAt(espiritu.getUpdatedAt());
        espirituJPA.setMediumConectado(mediumMapper.toJpa(espiritu.getMediumConectado()));
        return espirituJPA;
    }

    @Override
    public EspirituDemoniacoJPADTO toJpaDemonio(EspirituDemoniaco espiritu) {
        EspirituDemoniacoJPADTO espirituJPA = new EspirituDemoniacoJPADTO(espiritu.getNombre(), ubicacionMapper.toJpa(espiritu.getUbicacion()));
        espirituJPA.setId(espiritu.getId());
        espirituJPA.setDeleted(espiritu.isDeleted());
        espirituJPA.setCreatedAt(espiritu.getCreatedAt());
        espirituJPA.setUpdatedAt(espiritu.getUpdatedAt());
        espirituJPA.setMediumConectado(mediumMapper.toJpa(espiritu.getMediumConectado()));
        return espirituJPA;
    }

    @Override
    public List<Espiritu> toDomainList(List<EspirituJPADTO> espirituJPADTOS) {
        return espirituJPADTOS.stream().map(dto -> {
            switch (dto.getTipo()) {
                case ANGELICAL:
                    return toDomainAngel((EspirituAngelicalJPADTO) dto);
                case DEMONIACO:
                    return toDomainDemonio((EspirituDemoniacoJPADTO) dto);
                default:
                    throw new IllegalArgumentException("Tipo de espíritu desconocido: " + dto.getTipo());
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<EspirituDemoniaco> toDomainListDemoniaco(List<EspirituDemoniacoJPADTO> espirituDemoniacoJPADTOS) {
        return  espirituDemoniacoJPADTOS.stream()
                .map(this::toDomainDemonio)
                .collect(Collectors.toList());
    }

    @Override
    public List<EspirituAngelical> toDomainListAngelical(List<EspirituAngelicalJPADTO> espirituAngelicalJPADTOS) {
        return  espirituAngelicalJPADTOS.stream()
                .map(this::toDomainAngel)
                .collect(Collectors.toList());
    }

    @Override
    public EspirituAngelicalJPADTO actualizarEspirituAngelicalJpaCon(EspirituAngelicalJPADTO espirituJPADTO, EspirituAngelical espiritu) {
        espirituJPADTO.setNombre(espiritu.getNombre());
        espirituJPADTO.setUbicacion(ubicacionMapper.toJpa(espiritu.getUbicacion()));
//        espirituJPADTO.setId(espiritu.getId());
        espirituJPADTO.setDeleted(espiritu.isDeleted());
//        espirituJPADTO.setCreatedAt(espiritu.getCreatedAt());
//        espirituJPADTO.setUpdatedAt(espiritu.getUpdatedAt());
        espirituJPADTO.setMediumConectado(mediumMapper.toJpa(espiritu.getMediumConectado()));
        return espirituJPADTO;

    }

    @Override
    public EspirituDemoniacoJPADTO actualizarEspirituDemoniacoJpaCon(EspirituDemoniacoJPADTO espirituJPADTO, EspirituDemoniaco espiritu) {
        espirituJPADTO.setNombre(espiritu.getNombre());
        espirituJPADTO.setUbicacion(ubicacionMapper.toJpa(espiritu.getUbicacion()));
//        espirituJPADTO.setId(espiritu.getId());
        espirituJPADTO.setDeleted(espiritu.isDeleted());
//        espirituJPADTO.setCreatedAt(espiritu.getCreatedAt());
//        espirituJPADTO.setUpdatedAt(espiritu.getUpdatedAt());
        espirituJPADTO.setMediumConectado(mediumMapper.toJpa(espiritu.getMediumConectado()));
        return espirituJPADTO;
    }
}
