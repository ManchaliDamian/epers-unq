package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.exception.BadRequest.CoordenadaFueraDeAreaException;
import ar.edu.unq.epersgeist.exception.NotFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOSQL;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOFirestore;
import ar.edu.unq.epersgeist.persistencia.DAOs.PoligonoDAO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.EspirituMapper;

import org.hibernate.Hibernate;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.UbicacionMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EspirituRepositoryImpl implements EspirituRepository {

    private EspirituDAOSQL espirituDAOSQL;
    private EspirituDAOMongo espirituDAOMongo;
    private EspirituMapper mapperE;
    private PoligonoDAO poligonoDAOMongo;
    private EspirituDAOFirestore espirituDAOFirestore;

    public EspirituRepositoryImpl(
            EspirituDAOSQL espirituDAOSQL,
            EspirituDAOMongo espirituDAOMongo,
            EspirituDAOFirestore espirituDAOFirestore,
            PoligonoDAO poligonoDAOMongo,
            EspirituMapper mapperE
    ){
        this.espirituDAOSQL = espirituDAOSQL;
        this.espirituDAOMongo = espirituDAOMongo;
        this.espirituDAOFirestore = espirituDAOFirestore;
        this.poligonoDAOMongo = poligonoDAOMongo;
        this.mapperE = mapperE;
    }

    @Override
    public Espiritu guardar(Espiritu espiritu, Coordenada coordenada) {
        GeoJsonPoint punto = new GeoJsonPoint(coordenada.getLongitud(), coordenada.getLatitud());

        Optional<PoligonoMongoDTO> poligonoOpt = poligonoDAOMongo.findByPoligonoGeoIntersectsAndUbicacionId(punto, espiritu.getUbicacion().getId());
        if (poligonoOpt.isEmpty()) {
            throw new CoordenadaFueraDeAreaException("coordenada no valida");
        }

        // Crear en SQL
        EspirituJPADTO jpa = mapperE.toJpa(espiritu);
        jpa = espirituDAOSQL.save(jpa);

        // Crear en MongoDB
        EspirituMongoDTO mongoDTO = mapperE.toMongo(jpa, coordenada);
        espirituDAOMongo.save(mongoDTO);

        // Crear en Firestore
        espiritu.setId(jpa.getId());
        espirituDAOFirestore.crear(espiritu);

        return mapperE.toDomain(jpa);
    }

    @Override
    public Espiritu actualizar(Espiritu espiritu) {
        if (espiritu.getId() == null) {
            throw new IllegalArgumentException("El espiritu debe estar persistido");
        }

        // Actualizar en SQL
        EspirituJPADTO dto = this.actualizarEspirituJPA(espiritu);
        Espiritu actualizado = mapperE.toDomain(dto);

        // Actualizar en Firestore
        espirituDAOFirestore.actualizar(espiritu);
        espirituDAOFirestore.enriquecer(actualizado);

        return actualizado;
    }

    @Override
    public Espiritu actualizar(Espiritu espiritu, Coordenada coordenada) {
        if (espiritu.getId() == null) {
            throw new IllegalArgumentException("El espiritu debe estar persistido");
        }
        EspirituJPADTO dto = actualizarEspirituJPA(espiritu);
        Espiritu actualizado = mapperE.toDomain(dto);

        EspirituMongoDTO mongoDTOPersistido = espirituDAOMongo.findByIdSQL(espiritu.getId())
                .orElseThrow(() -> new EspirituNoEncontradoException(espiritu.getId()));

        // Actualizar en MongoDB
        EspirituMongoDTO mongoDTO = mapperE.toMongo(dto, coordenada);
        mongoDTO.setId(mongoDTOPersistido.getId());
        espirituDAOMongo.save(mongoDTO);

        // Actualizar en Firestore
        espirituDAOFirestore.actualizar(espiritu);
        espirituDAOFirestore.enriquecer(actualizado);

        return actualizado;
    }

    private EspirituJPADTO actualizarEspirituJPA(Espiritu espiritu) {
        espirituDAOSQL.findById(espiritu.getId())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new EspirituNoEncontradoException(espiritu.getId()));

        EspirituJPADTO dto = mapperE.toJpa(espiritu);
        espirituDAOSQL.save(dto);
        return dto;
    }

    @Override
    public void eliminar(Long id) { // este es para hard delete, no incluye sql
        // Eliminar de MongoDB
        Optional<EspirituMongoDTO> mongoDTO = espirituDAOMongo.findByIdSQL(id);
        mongoDTO.ifPresent(espirituDAOMongo::delete);

        // Eliminar de Firestore
        espirituDAOFirestore.eliminar(id);
    }


    @Override
    public List<Espiritu> recuperarTodos() {
        return mapperE.toDomainList(espirituDAOSQL.recuperarTodos());
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {

        Optional<Espiritu> optionalEspiritu = espirituDAOSQL.findById(espirituId)
                .map(dto -> (EspirituJPADTO) Hibernate.unproxy(dto))
                .map(mapperE::toDomain);

        optionalEspiritu.ifPresent(espirituDAOFirestore::enriquecer);

        return optionalEspiritu;
    }

    @Override
    public Optional<Coordenada> recuperarCoordenada(Long espirituId) {
        return espirituDAOMongo.findByIdSQL(espirituId).map(mapperE::toCoordenada);
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemonios() {
        return mapperE.toDomainListDemoniaco(espirituDAOSQL.recuperarDemonios());
    }

    @Override
    public List<EspirituAngelical> recuperarAngeles() {
        return mapperE.toDomainListAngelical(espirituDAOSQL.recuperarAngeles());
    }

    @Override
    public Optional<Espiritu> recuperarEliminado(Long id) {
        return espirituDAOSQL.recuperarEliminado(id).map(mapperE::toDomain);
    }

    @Override
    public List<Espiritu> recuperarTodosLosEliminados() {
        return mapperE.toDomainList(espirituDAOSQL.recuperarTodosLosEliminados());
    }

    @Override
    public List<EspirituAngelical> recuperarAngelesDe(Long mediumId) {
        return mapperE.toDomainListAngelical(espirituDAOSQL.recuperarAngelesDe(mediumId));
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId) {
        return mapperE.toDomainListDemoniaco(espirituDAOSQL.recuperarDemoniosDe(mediumId));
    }

    @Override
    public List<Espiritu> recuperarDemoniacosPaginados(Pageable pageable) {
        return mapperE.toDomainList(espirituDAOSQL.recuperarDemoniacosPaginados(pageable));
    }

    @Override
    public Optional<Double> distanciaA(Double longitud, Double latitud, Long idEspirituSQL) {
        return espirituDAOMongo.distanciaA(longitud,latitud,idEspirituSQL);
    }

    @Override
    public List<Espiritu> recuperarTodosMasyorVida(int vida) {
        return espirituDAOFirestore.recuperarTodosMayorVida(vida);
    }

    @Override
    public void deleteAll(){
        espirituDAOSQL.deleteAll();
        espirituDAOMongo.deleteAll();
        espirituDAOFirestore.deleteAll();
    }
}
