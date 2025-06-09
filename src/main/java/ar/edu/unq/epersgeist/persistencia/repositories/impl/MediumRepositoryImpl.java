package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.persistencia.DAOs.MediumDAOSQL;
import ar.edu.unq.epersgeist.persistencia.DAOs.MediumDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.EspirituMapper;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.MediumMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MediumRepositoryImpl implements MediumRepository {

    private MediumDAOSQL mediumDAOSQL;
    private MediumDAOMongo mediumDAOMongo;
    private MediumMapper mediumMapper;
    private EspirituMapper espirituMapper;

    public MediumRepositoryImpl(MediumDAOSQL mediumDAOSQL, MediumDAOMongo mediumDAOMongo, MediumMapper mediumMapper, EspirituMapper espirituMapper) {
        this.mediumDAOSQL = mediumDAOSQL;
        this.mediumMapper = mediumMapper;
        this.espirituMapper = espirituMapper;
        this.mediumDAOMongo = mediumDAOMongo;
    }

    @Override
    public Optional<Double> laDistanciaA(Double longitud, Double latitud, Long idMediumSQL){
        return mediumDAOMongo.distanciaA(longitud, latitud, idMediumSQL);
    }

    @Override
    public Medium save(Medium medium) {
        MediumJPADTO mediumGuardado = this.mediumDAOSQL.save(mediumMapper.toJpa(medium));
        Medium dominio = mediumMapper.toDomain(mediumGuardado);
        dominio.setCoordenada(medium.getCoordenada());

        MediumMongoDTO mongoDto = mediumMapper.toMongo(dominio);
        mongoDto.setIdSQL(mediumGuardado.getId());
        mediumDAOMongo.save(mongoDto);
        return dominio;
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
    }
}
