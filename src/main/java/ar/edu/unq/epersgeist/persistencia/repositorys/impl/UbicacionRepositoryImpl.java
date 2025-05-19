package ar.edu.unq.epersgeist.persistencia.repositorys.impl;

import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.persistencia.DAOs.UbicacionDAONeo;
import ar.edu.unq.epersgeist.persistencia.DAOs.UbicacionDAOSQL;
import ar.edu.unq.epersgeist.persistencia.repositorys.interfaces.UbicacionMapper;
import ar.edu.unq.epersgeist.persistencia.repositorys.interfaces.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private UbicacionDAONeo ubiDaoNeo;
    private UbicacionDAOSQL ubiSql;
    @Autowired
    private UbicacionMapper ubiMapper;
    public UbicacionRepositoryImpl(UbicacionDAONeo ubiDaoNeo, UbicacionDAOSQL ubiSql){
        this.ubiDaoNeo = ubiDaoNeo;
        this.ubiSql = ubiSql;
    }

    @Override
    public Ubicacion guardar(Ubicacion ubicacion){

        if (ubicacion.getTipo() == null) {
            throw new UbicacionNoEncontradaException(ubicacion.getId());
        }
        //Parte SQL
        UbicacionJPA ubiJPA = switch(ubicacion.getTipo()){
            case SANTUARIO -> new SantuarioJPA(ubicacion.getNombre(),
                                               ubicacion.getFlujoDeEnergia());
            case CEMENTERIO -> new CementerioJPA(ubicacion.getNombre(),
                                                ubicacion.getFlujoDeEnergia());
        };
        ubiSql.save(ubiJPA);

        //Parte Neo
        // se agrega el id del jpa para tener sincronizacion con la bd relacional y el tipo
        UbicacionNeo ubiNeo = new UbicacionNeo(ubiJPA.getId(), ubiJPA.getTipo());

        ubiDaoNeo.save(ubiNeo);

        // guardar la id en modelo para retornarlo
        ubicacion.setId(ubiJPA.getId());
        return ubicacion;
    }
    
    @Override
    public Ubicacion actualizar(Ubicacion ubicacion){
        return null;
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId){
        Optional<UbicacionJPA> ubicacionJPA = this.recuperarSql(ubicacionId);

        if (ubicacionJPA.isEmpty()) {
            throw new UbicacionNoEncontradaException(ubicacionId);
        }

        Ubicacion ubicacion = switch (ubicacionJPA.get().getTipo()) {
            case SANTUARIO -> new Santuario(ubicacionJPA.get().getNombre(), ubicacionJPA.get().getFlujoDeEnergia());
            case CEMENTERIO -> new Cementerio(ubicacionJPA.get().getNombre(), ubicacionJPA.get().getFlujoDeEnergia());
        };
        ubicacion.setId(ubicacionId);
        return Optional.of(ubicacion);
    }

    public Optional<UbicacionJPA> recuperarSql(Long ubicacionId){
        return ubiSql.findById(ubicacionId).filter(u -> !u.isDeleted());
    }

    //-------------------------------------------------------------------------

    //De acá para abajo duda sobre cuales serian también para neo4j

    @Override
    public List<Ubicacion> recuperarTodos(){
        return ubiSql.recuperarTodos(); //Duda si es con Sql o con Neo4j o ambas.
    }

    @Override
    public List<Cementerio> recuperarCementerios(){
        return ubiSql.recuperarCementerios();
    }

    @Override
    public List<Santuario> recuperarSantuarios(){
        return ubiSql.recuperarSantuarios();
    }

    @Override
    public List<Espiritu> findEspiritusByUbicacionId(Long id){
        return ubiSql.findEspiritusByUbicacionId(id);
    }

    @Override
    public List<Medium> findMediumsSinEspiritusByUbicacionId(Long id){
        return ubiSql.findMediumsSinEspiritusByUbicacionId(id);
    }

    @Override
    public List<Medium> findMediumByUbicacionId(Long ubicacionId){
        return ubiSql.findMediumByUbicacionId(ubicacionId);
    }
    //----------------------------------------------------------------

}
