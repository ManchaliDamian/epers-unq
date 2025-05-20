package ar.edu.unq.epersgeist.persistencia.repositorys.impl;

import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEliminableException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.persistencia.DAOs.UbicacionDAONeo;
import ar.edu.unq.epersgeist.persistencia.DAOs.UbicacionDAOSQL;
import ar.edu.unq.epersgeist.mapper.UbicacionMapper;
import ar.edu.unq.epersgeist.persistencia.UbicacionJPA.CementerioJPA;
import ar.edu.unq.epersgeist.persistencia.UbicacionJPA.SantuarioJPA;
import ar.edu.unq.epersgeist.persistencia.UbicacionJPA.UbicacionJPA;
import ar.edu.unq.epersgeist.persistencia.repositorys.interfaces.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        // --- SQL ---
        UbicacionJPA existente = ubiSql.findById(ubicacion.getId())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new UbicacionNoEncontradaException(ubicacion.getId()));

        // Actualizar los campos usando MapStruct
        ubiMapper.actualizarJPADesdeModelo(ubicacion, existente);
        ubiSql.save(existente);

//        // --- Neo4j ---
//        UbicacionNeo existenteNeo = ubiDaoNeo.findById(ubicacion.getId()).orElse(null);
//        if (existenteNeo != null) {
//            UbicacionNeo actualizado = ubiMapper.aNeo(ubicacion);
//            actualizado.setConexiones(existenteNeo.getConexiones()); // conservar conexiones si es necesario
//            ubiDaoNeo.save(actualizado);
//        }

        return ubicacion;
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
    public Optional<UbicacionNeo> recuperarNeo(Long ubicacionId){
        return ubiDaoNeo.findById(ubicacionId).filter(u -> !u.isDeleted());
    }
    @Override
    public void eliminar(Long id) {
        if (!ubiSql.findEspiritusByUbicacionId(id).isEmpty()
                || !ubiSql.findMediumByUbicacionId(id).isEmpty()
                || !ubiDaoNeo.tieneConexiones(id)) {
            throw new UbicacionNoEliminableException(id);
        }
        UbicacionJPA ubicacionSQLAEliminar = this.recuperarSql(id).orElseThrow(() -> new UbicacionNoEncontradaException(id));
        ubicacionSQLAEliminar.setDeleted(true);

        UbicacionNeo ubicacionNeoAEliminar = this.recuperarNeo(id).orElseThrow(() -> new UbicacionNoEncontradaException(id));
        ubicacionNeoAEliminar.setDeleted(true);


        ubiSql.save(ubicacionSQLAEliminar);
        ubiDaoNeo.save(ubicacionNeoAEliminar);
    }
    //-------------------------------------------------------------------------

    //De acá para abajo duda sobre cuales serian también para neo4j

    @Override
    public List<Ubicacion> recuperarTodos(){
        List<UbicacionJPA> ubicaciones = ubiSql.recuperarTodos();
        return ubiMapper.toModelListUbicacion(ubicaciones);     //Duda si es con Sql o con Neo4j o ambas.
    }

    @Override
    public List<Cementerio> recuperarCementerios(){
        List<CementerioJPA> ubicaciones = ubiSql.recuperarCementerios();
        return ubiMapper.toModelListCementerio(ubicaciones);
    }

    @Override
    public List<Santuario> recuperarSantuarios(){
        List<SantuarioJPA> ubicaciones = ubiSql.recuperarSantuarios();
        return ubiMapper.toModelListSantuarios(ubicaciones);
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
