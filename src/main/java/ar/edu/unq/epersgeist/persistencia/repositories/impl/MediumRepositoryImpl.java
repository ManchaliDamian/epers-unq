package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.persistencia.DAOs.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.EspirituMapper;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.MediumMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MediumRepositoryImpl implements MediumRepository {

    private MediumDAO mediumDAO;
    private MediumMapper mediumMapper;
    private EspirituMapper espirituMapper;

    public MediumRepositoryImpl(MediumDAO mediumDAO, @Qualifier("mediumMapperImpl") MediumMapper mediumMapper, EspirituMapper espirituMapper) {
        this.mediumDAO = mediumDAO;
        this.mediumMapper = mediumMapper;
        this.espirituMapper = espirituMapper;
    }

    @Override
    public Medium save(Medium medium) {
        MediumJPADTO mediumGuardado = this.mediumDAO.save(mediumMapper.toJpa(medium));
        return mediumMapper.toDomain(mediumGuardado);
    }

    @Override
    public Optional<Medium> findById(Long mediumId) {
        return this.mediumDAO.findById(mediumId).map(mediumJPADTO -> mediumMapper.toDomain(mediumJPADTO));
    }

    @Override
    public List<Medium> recuperarTodos() {
        return mediumMapper.toDomainList(this.mediumDAO.recuperarTodos());
    }

    @Override
    public Optional<Medium> recuperarEliminado(Long id) {
        return this.mediumDAO.recuperarEliminado(id).map(mediumJPADTO -> mediumMapper.toDomain(mediumJPADTO));
    }

    @Override
    public List<Medium> recuperarTodosLosEliminados() {
        return mediumMapper.toDomainList(this.mediumDAO.recuperarTodosLosEliminados());
    }

    @Override
    public List<Espiritu> findEspiritusByMediumId(Long mediumId) {
        return espirituMapper.toDomainList(this.mediumDAO.findEspiritusByMediumId(mediumId));
    }

    @Override
    public void deleteAll(){
        this.mediumDAO.deleteAll();
    }
}
