package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOSQL;
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

    private EspirituDAOSQL espirituDAOSQL;
    private EspirituDAOMongo espirituDAOMongo;
    private EspirituMapper mapper;

    public EspirituRepositoryImpl(EspirituDAOSQL espirituDAOSQL, EspirituDAOMongo espirituDAOMongo, EspirituMapper mapper){
        this.espirituDAOSQL = espirituDAOSQL;
        this.espirituDAOMongo = espirituDAOMongo;
        this.mapper = mapper;
    }

    @Override
    public Espiritu save(Espiritu espiritu) {
        EspirituJPADTO jpa = mapper.toJpa(espiritu);
        jpa = espirituDAOSQL.save(jpa);
        Espiritu dominio = mapper.toDomain(jpa);
        dominio.setCoordenada(espiritu.getCoordenada());

        EspirituMongoDTO mongoDto = mapper.toMongo(dominio);
        mongoDto.setIdSQL(jpa.getId());
        espirituDAOMongo.save(mongoDto);

        return dominio;
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return mapper.toDomainList(this.espirituDAOSQL.recuperarTodos());
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return this.espirituDAOSQL.findById(espirituId).map(espirituJPADTO -> mapper.toDomain(espirituJPADTO));
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemonios() {
        return mapper.toDomainListDemoniaco(this.espirituDAOSQL.recuperarDemonios());
    }

    @Override
    public List<EspirituAngelical> recuperarAngeles() {
        return mapper.toDomainListAngelical(this.espirituDAOSQL.recuperarAngeles());
    }

    @Override
    public Optional<Espiritu> recuperarEliminado(Long id) {
        return this.espirituDAOSQL.recuperarEliminado(id).map(espirituJPADTO -> mapper.toDomain(espirituJPADTO));
    }

    @Override
    public List<Espiritu> recuperarTodosLosEliminados() {
        return mapper.toDomainList(this.espirituDAOSQL.recuperarTodosLosEliminados());
    }

    @Override
    public List<EspirituAngelical> recuperarAngelesDe(Long mediumId) {
        return mapper.toDomainListAngelical(this.espirituDAOSQL.recuperarAngelesDe(mediumId));
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId) {
        return mapper.toDomainListDemoniaco(this.espirituDAOSQL.recuperarDemoniosDe(mediumId));
    }

    @Override
    public List<Espiritu> recuperarDemoniacosPaginados(Pageable pageable) {
        return mapper.toDomainList(this.espirituDAOSQL.recuperarDemoniacosPaginados(pageable));
    }

    @Override
    public void deleteAll(){
        this.espirituDAOSQL.deleteAll();
    }
}
