package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEliminableException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.persistencia.DAOs.UbicacionDAONeo;
import ar.edu.unq.epersgeist.persistencia.DAOs.UbicacionDAOSQL;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.CementerioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.SantuarioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.UbicacionMapper;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private UbicacionDAONeo ubiDaoNeo;
    private UbicacionDAOSQL ubiDaoSQL;
    private UbicacionMapper ubiMapper;

    public UbicacionRepositoryImpl(UbicacionDAONeo ubiDaoNeo, UbicacionDAOSQL ubiDaoSql, @Qualifier("ubicacionMapper") UbicacionMapper ubiMapper){
        this.ubiDaoNeo = ubiDaoNeo;
        this.ubiDaoSQL = ubiDaoSql;
        this.ubiMapper = ubiMapper;
    }

    @Override
    public Ubicacion guardar(Ubicacion ubicacion){
        // SQL
        UbicacionJPADTO ubiJPA = ubiMapper.toJpa(ubicacion);
        ubiDaoSQL.save(ubiJPA);

        ubicacion.setId(ubiJPA.getId());

        // Neo
        ubiDaoNeo.save(ubiMapper.toNeo(ubicacion));

        return ubicacion;
    }

    @Override
    public Ubicacion actualizar(Ubicacion ubicacion){
        // --- SQL ---
        UbicacionJPADTO ubiJPA = ubiDaoSQL.findById(ubicacion.getId())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new UbicacionNoEncontradaException(ubicacion.getId()));

        ubiJPA = ubiMapper.actualizarJpaCon(ubiJPA, ubicacion);

        ubiDaoSQL.save(ubiJPA);
        ubicacion.setId(ubiJPA.getId());

        // --- Neo4j ---
        ubiDaoNeo.save(ubiMapper.toNeo(ubicacion));
        return ubicacion;
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId){
        Optional<UbicacionJPADTO> ubicacionJPA = this.recuperarSql(ubicacionId);

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

    public Optional<UbicacionJPADTO> recuperarSql(Long ubicacionId){
        return ubiDaoSQL.findById(ubicacionId).filter(u -> !u.isDeleted());
    }
    public Optional<UbicacionNeoDTO> recuperarNeo(Long ubicacionId){
        return ubiDaoNeo.findById(ubicacionId).filter(u -> !u.isDeleted());
    }
    @Override
    public void eliminar(Long id) {
        if (!ubiDaoSQL.findEspiritusByUbicacionId(id).isEmpty()
                || !ubiDaoSQL.findMediumByUbicacionId(id).isEmpty()
                || !ubiDaoNeo.tieneConexiones(id)) {
            throw new UbicacionNoEliminableException(id);
        }
        UbicacionJPADTO ubicacionSQLAEliminar = this.recuperarSql(id).orElseThrow(() -> new UbicacionNoEncontradaException(id));
        ubicacionSQLAEliminar.setDeleted(true);

        UbicacionNeoDTO ubicacionNeoAEliminar = this.recuperarNeo(id).orElseThrow(() -> new UbicacionNoEncontradaException(id));
        ubicacionNeoAEliminar.setDeleted(true);


        ubiDaoSQL.save(ubicacionSQLAEliminar);
        ubiDaoNeo.save(ubicacionNeoAEliminar);
    }
    //-------------------------------------------------------------------------

    //De acá para abajo duda sobre cuales serian también para neo4j

    @Override
    public List<Ubicacion> recuperarTodos(){
        List<UbicacionJPADTO> ubicaciones = ubiDaoSQL.recuperarTodos();
        return ubiMapper.toModelListUbicacion(ubicaciones);     //Duda si es con Sql o con Neo4j o ambas.
    }

    @Override
    public List<Cementerio> recuperarCementerios(){
        List<CementerioJPADTO> ubicaciones = ubiDaoSQL.recuperarCementerios();
        return ubiMapper.toModelListCementerio(ubicaciones);
    }

    @Override
    public List<Santuario> recuperarSantuarios(){
        List<SantuarioJPADTO> ubicaciones = ubiDaoSQL.recuperarSantuarios();
        return ubiMapper.toModelListSantuarios(ubicaciones);
    }

    @Override
    public List<Espiritu> findEspiritusByUbicacionId(Long id){
        return ubiDaoSQL.findEspiritusByUbicacionId(id);
    }

    @Override
    public List<Medium> findMediumsSinEspiritusByUbicacionId(Long id){
        return ubiDaoSQL.findMediumsSinEspiritusByUbicacionId(id);
    }

    @Override
    public List<Medium> findMediumByUbicacionId(Long ubicacionId){
        return ubiDaoSQL.findMediumByUbicacionId(ubicacionId);
    }
    //----------------------------------------------------------------

}
