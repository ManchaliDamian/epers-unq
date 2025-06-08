package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.exception.CoordenadaFueraDeAreaException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOSQL;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DAOs.PoligonoDAO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.EspirituMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EspirituRepositoryImpl implements EspirituRepository {

    private EspirituDAOSQL espirituDAOSQL;
    private EspirituDAOMongo espirituDAOMongo;
    private EspirituMapper mapper;
    private PoligonoDAO poligonoDAO;


    public EspirituRepositoryImpl(EspirituDAOSQL espirituDAOSQL, EspirituDAOMongo espirituDAOMongo,
                                  EspirituMapper mapper, PoligonoDAO poligonoDAO){
        this.espirituDAOSQL = espirituDAOSQL;
        this.espirituDAOMongo = espirituDAOMongo;
        this.mapper = mapper;
        this.poligonoDAO = poligonoDAO;
    }

    @Override
    public Espiritu save(Espiritu espiritu) {
        EspirituJPADTO jpa = mapper.toJpa(espiritu);
        jpa = espirituDAOSQL.save(jpa);
        espiritu.setId(jpa.getId());
        espiritu.setCreatedAt(jpa.getCreatedAt());
        espiritu.setUpdatedAt(jpa.getUpdatedAt());

        GeoJsonPoint punto = new GeoJsonPoint(
                espiritu.getCoordenada().getLongitud(),
                espiritu.getCoordenada().getLatitud()
        );

        Optional<PoligonoMongoDTO> areaOpt =
                poligonoDAO.findByPoligonoGeoIntersects(punto);


        if (areaOpt.isEmpty()
                || !areaOpt.get().getUbicacionId().equals(espiritu.getUbicacion().getId())) {
            throw new CoordenadaFueraDeAreaException(
                    "El punto " + punto + " no pertenece al área de la ubicación "
                            + espiritu.getUbicacion().getId()
            );
        }

        EspirituMongoDTO mDto = mapper.toMongo(espiritu);
        mDto.setIdSQL(jpa.getId());
        espirituDAOMongo.save(mDto);

        espiritu.setCoordenada(mapper.toCoordenada(mDto));

        return espiritu;
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
