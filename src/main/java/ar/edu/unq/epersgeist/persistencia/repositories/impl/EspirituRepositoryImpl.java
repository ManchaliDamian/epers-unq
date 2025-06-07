package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.EspirituMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EspirituRepositoryImpl implements EspirituRepository {

    private EspirituDAO espirituDAO;
    private EspirituDAOMongo espirituDAOMongo;
    private EspirituMapper mapper;

    public EspirituRepositoryImpl(EspirituDAO espirituDAO, EspirituMapper mapper){
        this.espirituDAO = espirituDAO;
        this.mapper = mapper;
    }

    @Override
    public Espiritu save(Espiritu espiritu) {
        EspirituJPADTO jpa = mapper.toJpa(espiritu);
        jpa = espirituDAO.save(jpa);
        Espiritu dominio = mapper.toDomain(jpa);

        EspirituMongoDTO mongoDto = mapper.toMongo(dominio);
        mongoDto.setIdSQL(dominio.getId());
        espirituDAOMongo.save(mongoDto);

        dominio.setCoordenada(mapper.toCoordenada(mongoDto));

        return dominio;
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return mapper.toDomainList(this.espirituDAO.recuperarTodos());
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return this.espirituDAO.findById(espirituId).map(espirituJPADTO -> mapper.toDomain(espirituJPADTO));
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemonios() {
        return mapper.toDomainListDemoniaco(this.espirituDAO.recuperarDemonios());
    }

    @Override
    public List<EspirituAngelical> recuperarAngeles() {
        return mapper.toDomainListAngelical(this.espirituDAO.recuperarAngeles());
    }

    @Override
    public Optional<Espiritu> recuperarEliminado(Long id) {
        return this.espirituDAO.recuperarEliminado(id).map(espirituJPADTO -> mapper.toDomain(espirituJPADTO));
    }

    @Override
    public List<Espiritu> recuperarTodosLosEliminados() {
        return mapper.toDomainList(this.espirituDAO.recuperarTodosLosEliminados());
    }

    @Override
    public List<EspirituAngelical> recuperarAngelesDe(Long mediumId) {
        return mapper.toDomainListAngelical(this.espirituDAO.recuperarAngelesDe(mediumId));
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId) {
        return mapper.toDomainListDemoniaco(this.espirituDAO.recuperarDemoniosDe(mediumId));
    }

    @Override
    public List<Espiritu> recuperarDemoniacosPaginados(Pageable pageable) {
        return mapper.toDomainList(this.espirituDAO.recuperarDemoniacosPaginados(pageable));
    }

    @Override
    public void deleteAll(){
        this.espirituDAO.deleteAll();
    }
}
