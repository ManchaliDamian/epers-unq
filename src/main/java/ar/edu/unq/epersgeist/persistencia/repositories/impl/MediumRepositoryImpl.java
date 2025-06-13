package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.exception.CoordenadaFueraDeAreaException;
import ar.edu.unq.epersgeist.exception.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DAOs.MediumDAOSQL;
import ar.edu.unq.epersgeist.persistencia.DAOs.MediumDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DAOs.PoligonoDAO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.EspirituMapper;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.MediumMapper;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MediumRepositoryImpl implements MediumRepository {

    private MediumDAOSQL mediumDAOSQL;
    private MediumDAOMongo mediumDAOMongo;
    private MediumMapper mediumMapper;
    private EspirituMapper espirituMapper;
    private PoligonoDAO poligonoDAOMongo;

    public MediumRepositoryImpl(MediumDAOSQL mediumDAOSQL, MediumDAOMongo mediumDAOMongo, MediumMapper mediumMapper, EspirituMapper espirituMapper, PoligonoDAO poligonoDAOMongo) {
        this.mediumDAOSQL = mediumDAOSQL;
        this.mediumMapper = mediumMapper;
        this.espirituMapper = espirituMapper;
        this.mediumDAOMongo = mediumDAOMongo;
        this.poligonoDAOMongo = poligonoDAOMongo;
    }

    @Override
    public Optional<Double> distanciaA(Double latitud, Double longitud, Long idMediumSQL){
        return mediumDAOMongo.distanciaA(latitud, longitud, idMediumSQL);
    }

    @Override
    public Medium guardar(Medium medium, Coordenada coordenada) {
        GeoJsonPoint punto = new GeoJsonPoint(coordenada.getLongitud(), coordenada.getLatitud());

        Optional<PoligonoMongoDTO> poligonoOpt = poligonoDAOMongo.findByPoligonoGeoIntersectsAndUbicacionId(punto, medium.getUbicacion().getId());
        if (poligonoOpt.isEmpty()) {
            throw new CoordenadaFueraDeAreaException("coordenada no valida");
        }
        MediumJPADTO jpa = this.mediumDAOSQL.save(mediumMapper.toJpa(medium));
        MediumMongoDTO mongoDto = mediumMapper.toMongo(jpa, coordenada);
        mediumDAOMongo.save(mongoDto);
        return mediumMapper.toDomain(jpa);
    }

    @Override
    public Medium actualizar(Medium medium) {
        if (medium.getId() == null) {
            throw new IllegalArgumentException("El medium debe estar persistido");
        }
        MediumJPADTO actualizar = actualizarMediumJPA(medium);
        return  mediumMapper.toDomain(actualizar);
    }

    @Override
    public Medium actualizar(Medium medium, Coordenada coordenada) {
        if (medium.getId() == null) {
            throw new IllegalArgumentException("El medium debe estar persistido");
        }
        MediumJPADTO dto = actualizarMediumJPA(medium);

        // eliminar la coordenada anterior
        mediumDAOMongo.deleteByIdSQL(medium.getId());

        // crear nuevo document
        MediumMongoDTO mongoDTO = mediumMapper.toMongo(dto, coordenada);
        mediumDAOMongo.save(mongoDTO);
        return mediumMapper.toDomain(dto);
    }

    @Override
    public void eliminarFisicoEnMongoSiExiste(Long id) {
        Optional<MediumMongoDTO> mongoDTO = mediumDAOMongo.findByIdSQL(id);
        mongoDTO.ifPresent(mediumDAOMongo::delete);
    }

    private MediumJPADTO actualizarMediumJPA(Medium medium) {
        mediumDAOSQL.findById(medium.getId())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new EspirituNoEncontradoException(medium.getId()));

        MediumJPADTO dto = mediumMapper.toJpa(medium);
        mediumDAOSQL.save(dto);
        return dto;
    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {
        return this.mediumDAOSQL.findById(mediumId).map(mediumJPADTO -> mediumMapper.toDomain(mediumJPADTO));
    }

    @Override
    public List<Medium> recuperarTodos() {
        return mediumMapper.toDomainList(this.mediumDAOSQL.recuperarTodos());
    }

    @Override
    public Optional<Medium> recuperarEliminado(Long id) {
        return this.mediumDAOSQL.recuperarEliminado(id).map(mediumJPADTO -> mediumMapper.toDomain(mediumJPADTO));
    }

    @Override
    public List<Medium> recuperarTodosLosEliminados() {
        return mediumMapper.toDomainList(this.mediumDAOSQL.recuperarTodosLosEliminados());
    }

    @Override
    public List<Espiritu> findEspiritusByMediumId(Long mediumId) {
        return espirituMapper.toDomainList(this.mediumDAOSQL.findEspiritusByMediumId(mediumId));
    }

    @Override
    public void deleteAll(){
        this.mediumDAOSQL.deleteAll();
        this.mediumDAOMongo.deleteAll();
    }
}
