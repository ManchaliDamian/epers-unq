package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.*;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.*;

@Component("espirituMapperImpl")
public class EspirituMapperImp implements EspirituMapper {
    private final UbicacionMapper ubicacionMapper;
    private final MediumMapper mediumMapper;

    public EspirituMapperImp(UbicacionMapper ubicacionMapper, @Lazy MediumMapper mediumMapper) {
        this.ubicacionMapper = ubicacionMapper;
        this.mediumMapper = mediumMapper;
    }

    @Override
    public EspirituAngelical toDomainAngel(EspirituAngelicalJPADTO jpa) {
        return toDomainAngel(jpa, new IdentityHashMap<>());
    }

    @Override
    public EspirituDemoniaco toDomainDemonio(EspirituDemoniacoJPADTO jpa) {
        return toDomainDemonio(jpa, new IdentityHashMap<>());
    }

    @Override
    public EspirituAngelicalJPADTO toJpaAngel(EspirituAngelical espiritu) {
        return toJpaAngel(espiritu, new IdentityHashMap<>());
    }

    @Override
    public EspirituDemoniacoJPADTO toJpaDemonio(EspirituDemoniaco espiritu) {
        return toJpaDemonio(espiritu, new IdentityHashMap<>());
    }

    @Override
    public List<Espiritu> toDomainList(List<EspirituJPADTO> espirituJPADTOS) {
        return toDomainList(espirituJPADTOS, new IdentityHashMap<>());
    }

    @Override
    public List<EspirituJPADTO> toJPAList(List<Espiritu> espiritus) {
        if (espiritus == null) return Collections.emptyList();
        return toJPAList(espiritus, new IdentityHashMap<>());
    }

    public EspirituAngelical toDomainAngel(EspirituAngelicalJPADTO jpa, Map<Object, Object> context) {
        if (jpa == null) return null;
        if (context.containsKey(jpa)) return (EspirituAngelical) context.get(jpa);

        EspirituAngelical espiritu = new EspirituAngelical(jpa.getNombre(), ubicacionMapper.toDomain(jpa.getUbicacion()));
        context.put(jpa, espiritu);

        espiritu.setId(jpa.getId());
        espiritu.setDeleted(jpa.isDeleted());
        espiritu.setCreatedAt(jpa.getCreatedAt());
        espiritu.setUpdatedAt(jpa.getUpdatedAt());
        espiritu.setNivelDeConexion(jpa.getNivelDeConexion());
        if (jpa.getMediumConectado() != null) {
            espiritu.setMediumConectado(mediumMapper.toDomain(jpa.getMediumConectado(), context));
        }
        if (jpa.getDominador() != null) {
            espiritu.setDominador(toDomain((EspirituJPADTO) jpa.getDominador(), context));
        }
        return espiritu;
    }

    public EspirituDemoniaco toDomainDemonio(EspirituDemoniacoJPADTO jpa, Map<Object, Object> context) {
        if (jpa == null) return null;
        if (context.containsKey(jpa)) return (EspirituDemoniaco) context.get(jpa);

        EspirituDemoniaco espiritu = new EspirituDemoniaco(jpa.getNombre(), ubicacionMapper.toDomain(jpa.getUbicacion()));
        context.put(jpa, espiritu);

        espiritu.setId(jpa.getId());
        espiritu.setDeleted(jpa.isDeleted());
        espiritu.setCreatedAt(jpa.getCreatedAt());
        espiritu.setUpdatedAt(jpa.getUpdatedAt());
        espiritu.setNivelDeConexion(jpa.getNivelDeConexion());
        if (jpa.getMediumConectado() != null) {
            espiritu.setMediumConectado(mediumMapper.toDomain(jpa.getMediumConectado(), context)); // Pasar contexto
        }
        if (jpa.getDominador() != null) {
            espiritu.setDominador(toDomain((EspirituJPADTO) jpa.getDominador(), context));
        }
        return espiritu;
    }

    @Override
    public Espiritu toDomain(EspirituJPADTO jpa, Map<Object, Object> context) {
        if (jpa == null) {
            return null;
        }
        if (context.containsKey(jpa)) {
            return (Espiritu) context.get(jpa);
        }

        // Desproxificar antes del switch
        EspirituJPADTO dto = (EspirituJPADTO) Hibernate.unproxy(jpa);

        return switch (dto.getTipo()) {
            case ANGELICAL -> toDomainAngel((EspirituAngelicalJPADTO) dto, context);
            case DEMONIACO -> toDomainDemonio((EspirituDemoniacoJPADTO) dto, context);
        };
    }

    public EspirituAngelicalJPADTO toJpaAngel(EspirituAngelical espiritu, Map<Object, Object> context) {
        if (espiritu == null) return null;
        if (context.containsKey(espiritu)) return (EspirituAngelicalJPADTO) context.get(espiritu);

        EspirituAngelicalJPADTO espirituJPA = new EspirituAngelicalJPADTO(espiritu.getNombre(), ubicacionMapper.toJpa(espiritu.getUbicacion()));
        context.put(espiritu, espirituJPA);

        espirituJPA.setId(espiritu.getId());
        espirituJPA.setDeleted(espiritu.isDeleted());
        espirituJPA.setCreatedAt(espiritu.getCreatedAt());
        espirituJPA.setUpdatedAt(espiritu.getUpdatedAt());
        espirituJPA.setNivelDeConexion(espiritu.getNivelDeConexion());
        if (espiritu.getMediumConectado() != null) {
            espirituJPA.setMediumConectado(mediumMapper.toJpa(espiritu.getMediumConectado(), context)); // Pasar contexto
        }
        if (espiritu.getDominador() != null) {
            espirituJPA.setDominador(toJpa(espiritu.getDominador(), context));
        }
        return espirituJPA;
    }

    @Override
    public EspirituDemoniacoJPADTO toJpaDemonio(EspirituDemoniaco espiritu, Map<Object, Object> context) {
        if (espiritu == null) return null;
        if (context.containsKey(espiritu)) return (EspirituDemoniacoJPADTO) context.get(espiritu);

        EspirituDemoniacoJPADTO espirituJPA = new EspirituDemoniacoJPADTO(espiritu.getNombre(), ubicacionMapper.toJpa(espiritu.getUbicacion()));
        context.put(espiritu, espirituJPA);

        espirituJPA.setId(espiritu.getId());
        espirituJPA.setDeleted(espiritu.isDeleted());
        espirituJPA.setCreatedAt(espiritu.getCreatedAt());
        espirituJPA.setUpdatedAt(espiritu.getUpdatedAt());
        espirituJPA.setNivelDeConexion(espiritu.getNivelDeConexion());
        if (espiritu.getMediumConectado() != null) {
            espirituJPA.setMediumConectado(mediumMapper.toJpa(espiritu.getMediumConectado(), context)); // Pasar contexto
        }
        if (espiritu.getDominador() != null) {
            espirituJPA.setDominador(toJpa(espiritu.getDominador(), context));
        }
        return espirituJPA;
    }

    @Override
    public EspirituJPADTO toJpa(Espiritu espiritu, Map<Object, Object> context) {
        if (espiritu == null) return null;
        if (context.containsKey(espiritu)) {
            return (EspirituJPADTO) context.get(espiritu);
        }

        return switch (espiritu.getTipo()) {
            case ANGELICAL -> toJpaAngel((EspirituAngelical) espiritu, context);
            case DEMONIACO -> toJpaDemonio((EspirituDemoniaco) espiritu, context);
        };
    }

    @Override
    public List<Espiritu> toDomainList(List<EspirituJPADTO> espirituJPADTOS, Map<Object, Object> context) {
        if (espirituJPADTOS == null) return Collections.emptyList();
        return espirituJPADTOS.stream().map(dto -> {
            return switch (dto.getTipo()) {
                case ANGELICAL -> toDomainAngel((EspirituAngelicalJPADTO) dto, context);
                case DEMONIACO -> toDomainDemonio((EspirituDemoniacoJPADTO) dto, context);
            };
        }).collect(Collectors.toList());
    }

    @Override
    public List<EspirituJPADTO> toJPAList(List<Espiritu> espiritus, Map<Object, Object> context) {
        if (espiritus == null) return Collections.emptyList();
        return espiritus.stream().map(espiritu -> {
            if (espiritu instanceof EspirituAngelical) {
                return toJpaAngel((EspirituAngelical) espiritu, context);
            } else if (espiritu instanceof EspirituDemoniaco) {
                return toJpaDemonio((EspirituDemoniaco) espiritu, context);
            }
            throw new IllegalArgumentException("Tipo de espíritu desconocido en lista: " + espiritu.getClass().getName());
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
        espirituJPADTO.setDeleted(espiritu.isDeleted());
        if (espiritu.getMediumConectado() != null) {

            espirituJPADTO.setMediumConectado(mediumMapper.toJpa(espiritu.getMediumConectado()));
        } else {
            espirituJPADTO.setMediumConectado(null);
        }
        return espirituJPADTO;
    }

    @Override
    public EspirituDemoniacoJPADTO actualizarEspirituDemoniacoJpaCon(EspirituDemoniacoJPADTO espirituJPADTO, EspirituDemoniaco espiritu) {
        espirituJPADTO.setNombre(espiritu.getNombre());
        espirituJPADTO.setUbicacion(ubicacionMapper.toJpa(espiritu.getUbicacion()));
        espirituJPADTO.setDeleted(espiritu.isDeleted());
        if (espiritu.getMediumConectado() != null) {
            espirituJPADTO.setMediumConectado(mediumMapper.toJpa(espiritu.getMediumConectado()));
        } else {
            espirituJPADTO.setMediumConectado(null);
        }
        return espirituJPADTO;
    }

    @Override
    public EspirituMongoDTO toMongo(EspirituJPADTO jpa, Coordenada coordenada) {
        GeoJsonPoint punto = new GeoJsonPoint(coordenada.getLongitud(), coordenada.getLatitud());
        EspirituMongoDTO dto = new EspirituMongoDTO(punto);
        dto.setIdSQL(jpa.getId());
        dto.setMediumConectadoIdSQL(jpa.getMediumConectado() != null ? jpa.getMediumConectado().getId() : null);
        return dto;
    }

    @Override
    public Coordenada toCoordenada(EspirituMongoDTO mongo) {
        GeoJsonPoint p = mongo.getPunto();
        return new Coordenada(p.getY(), p.getX());
    }

    @Override
    public Espiritu addFirestoreData(Espiritu sinFirestoreData, Espiritu conFirestoreData) {
        sinFirestoreData.setAtaque(conFirestoreData.getAtaque());
        sinFirestoreData.setDefensa(conFirestoreData.getDefensa());
        sinFirestoreData.setVida(conFirestoreData.getVida());
        sinFirestoreData.setBatallasGanadas(conFirestoreData.getBatallasGanadas());
        sinFirestoreData.setBatallasPerdidas(conFirestoreData.getBatallasPerdidas());
        sinFirestoreData.setBatallasJugadas(conFirestoreData.getBatallasJugadas());
        return sinFirestoreData;
    }
}