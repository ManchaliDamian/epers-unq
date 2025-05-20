package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.EspirituMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EspirituRepositoryImpl implements EspirituRepository {

    private EspirituDAO espirituDAO;
    private EspirituMapper mapper;

    public EspirituRepositoryImpl(EspirituDAO espirituDAO, @Qualifier("espirituMapper") EspirituMapper mapper){
        this.espirituDAO = espirituDAO;
        this.mapper = mapper;
    }

    @Override
    public Espiritu save(Espiritu espiritu) {
        return mapper.toDomain(this.espirituDAO.save(mapper.toJpa(espiritu)));
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return this.espirituDAO.recuperarTodos();
    }

    @Override
    public Optional<Espiritu> findById(Long espirituId) {
        return this.espirituDAO.findById(espirituId).map(espirituJPADTO -> mapper.toDomain(espirituJPADTO));
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemonios() {
        return this.espirituDAO.recuperarDemonios();
    }

    @Override
    public List<EspirituAngelical> recuperarAngeles() {
        return this.espirituDAO.recuperarAngeles();
    }

    @Override
    public Optional<Espiritu> recuperarEliminado(Long id) {
        return this.espirituDAO.recuperarEliminado(id);
    }

    @Override
    public List<Espiritu> recuperarTodosLosEliminados() {
        return this.espirituDAO.recuperarTodosLosEliminados();
    }

    @Override
    public List<EspirituAngelical> recuperarAngelesDe(Long mediumId) {
        return this.espirituDAO.recuperarAngelesDe(mediumId);
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId) {
        return this.espirituDAO.recuperarDemoniosDe(mediumId);
    }

    @Override
    public List<Espiritu> recuperarDemoniacosPaginados(Pageable pageable) {
        return this.espirituDAO.recuperarDemoniacosPaginados(pageable);
    }
}
