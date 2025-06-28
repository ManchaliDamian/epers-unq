package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.exception.BadRequest.CoordenadaFueraDeAreaException;
import ar.edu.unq.epersgeist.exception.NotFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOSQL;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituFirebaseDAO;
import ar.edu.unq.epersgeist.persistencia.DAOs.PoligonoDAO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.EspirituMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;

import com.google.cloud.firestore.WriteResult;
import org.hibernate.Hibernate;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.UbicacionMapper;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EspirituRepositoryImpl implements EspirituRepository {

    private EspirituDAOSQL espirituDAOSQL;
    private EspirituDAOMongo espirituDAOMongo;
    private EspirituMapper mapperE;
    private UbicacionMapper mapperU;
    private EspirituMapper mapper;
    private PoligonoDAO poligonoDAOMongo;
    @Autowired
    private EspirituFirebaseDAO espirituFirebaseDAO;

    public EspirituRepositoryImpl(EspirituDAOSQL espirituDAOSQL, EspirituDAOMongo espirituDAOMongo, EspirituMapper mapperE, PoligonoDAO poligonoDAOMongo,UbicacionMapper mapperU){
        this.espirituDAOSQL = espirituDAOSQL;
        this.espirituDAOMongo = espirituDAOMongo;
        this.poligonoDAOMongo = poligonoDAOMongo;
        this.mapperE = mapperE;
        this.mapperU = mapperU;
    }

    @Override
    public Espiritu guardar(Espiritu espiritu, Coordenada coordenada) {
        GeoJsonPoint punto = new GeoJsonPoint(coordenada.getLongitud(), coordenada.getLatitud());

        Optional<PoligonoMongoDTO> poligonoOpt = poligonoDAOMongo.findByPoligonoGeoIntersectsAndUbicacionId(punto, espiritu.getUbicacion().getId());
        if (poligonoOpt.isEmpty()) {
            throw new CoordenadaFueraDeAreaException("coordenada no valida");
        }
        EspirituJPADTO jpa = mapperE.toJpa(espiritu);
        jpa = espirituDAOSQL.save(jpa);

        EspirituMongoDTO mongoDTO = mapperE.toMongo(jpa, coordenada);
        espirituDAOMongo.save(mongoDTO);

        //FIREBASE
        try {
            espirituFirebaseDAO.save(mapperE.toDomain(jpa));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("La operación de guardado en Firebase fue interrumpida para el espíritu ID: " + espiritu.getId());
            // Decide el manejo de errores: loggear, lanzar una excepción, etc.
        } catch (ExecutionException e) {
            System.err.println("Error al guardar en Firebase para el espíritu ID: " + espiritu.getId() + ": " + e.getCause().getMessage());
            // Decide el manejo de errores
        }

        return mapperE.toDomain(jpa);
    }

    @Override
    public Espiritu actualizar(Espiritu espiritu) {
        if (espiritu.getId() == null) {
            throw new IllegalArgumentException("El espiritu debe estar persistido");
        }
        EspirituJPADTO dto = actualizarEspirituJPA(espiritu);
        return mapperE.toDomain(dto);
    }

    @Override
    public Espiritu actualizar(Espiritu espiritu, Coordenada coordenada) {
        if (espiritu.getId() == null) {
            throw new IllegalArgumentException("El espiritu debe estar persistido");
        }
        EspirituJPADTO dto = actualizarEspirituJPA(espiritu);

        espirituDAOMongo.deleteByIdSQL(espiritu.getId());

        EspirituMongoDTO mongoDTO = mapperE.toMongo(dto, coordenada);
        espirituDAOMongo.save(mongoDTO);
        return mapperE.toDomain(dto);
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
    public void eliminarFisicoEnMongoSiExiste(Long id) {
        Optional<EspirituMongoDTO> mongoDTO = espirituDAOMongo.findByIdSQL(id);
        mongoDTO.ifPresent(espirituDAOMongo::delete);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return mapperE.toDomainList(this.espirituDAOSQL.recuperarTodos());
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return this.espirituDAOSQL.findById(espirituId).map(espirituJPADTO -> {
            EspirituJPADTO realJPA = (EspirituJPADTO) Hibernate.unproxy(espirituJPADTO);
            return mapperE.toDomain(realJPA);
        });

    }

    @Override
    public Optional<Coordenada> recuperarCoordenada(Long espirituId) {
        return espirituDAOMongo.findByIdSQL(espirituId).map(espirituMongoDTO -> mapperE.toCoordenada(espirituMongoDTO));
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemonios() {
        return mapperE.toDomainListDemoniaco(this.espirituDAOSQL.recuperarDemonios());
    }

    @Override
    public List<EspirituAngelical> recuperarAngeles() {
        return mapperE.toDomainListAngelical(this.espirituDAOSQL.recuperarAngeles());
    }

    @Override
    public Optional<Espiritu> recuperarEliminado(Long id) {
        return this.espirituDAOSQL.recuperarEliminado(id).map(espirituJPADTO -> mapperE.toDomain(espirituJPADTO));
    }

    @Override
    public List<Espiritu> recuperarTodosLosEliminados() {
        return mapperE.toDomainList(this.espirituDAOSQL.recuperarTodosLosEliminados());
    }

    @Override
    public List<EspirituAngelical> recuperarAngelesDe(Long mediumId) {
        return mapperE.toDomainListAngelical(this.espirituDAOSQL.recuperarAngelesDe(mediumId));
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId) {
        return mapperE.toDomainListDemoniaco(this.espirituDAOSQL.recuperarDemoniosDe(mediumId));
    }

    @Override
    public List<Espiritu> recuperarDemoniacosPaginados(Pageable pageable) {
        return mapperE.toDomainList(this.espirituDAOSQL.recuperarDemoniacosPaginados(pageable));
    }
    @Override
    public Optional<Double> distanciaA(Double longitud, Double latitud, Long idEspirituSQL) {
        return espirituDAOMongo.distanciaA(longitud,latitud,idEspirituSQL);
    }
    @Override
    public void deleteAll(){
        this.espirituDAOSQL.deleteAll();
        this.espirituDAOMongo.deleteAll();
    }
}
