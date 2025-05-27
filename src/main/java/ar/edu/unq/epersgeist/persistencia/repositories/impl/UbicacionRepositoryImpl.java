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
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.EspirituMapper;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.MediumMapper;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.UbicacionMapper;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;

import ar.edu.unq.epersgeist.servicios.interfaces.ClosenessResult;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private UbicacionDAONeo ubiDaoNeo;
    private UbicacionDAOSQL ubiDaoSQL;
    private UbicacionMapper mapperU;
    private MediumMapper mapperM;
    private EspirituMapper mapperE;

    public UbicacionRepositoryImpl(UbicacionDAONeo ubiDaoNeo, UbicacionDAOSQL ubiDaoSql,
                                   MediumMapper mapperM, EspirituMapper mapperE,
                                   UbicacionMapper mapperU){
        this.ubiDaoNeo = ubiDaoNeo;
        this.ubiDaoSQL = ubiDaoSql;
        this.mapperU = mapperU;
        this.mapperE = mapperE;
        this.mapperM = mapperM;
    }

    @Override
    public Ubicacion guardar(Ubicacion ubicacion){
        // SQL
        UbicacionJPADTO ubiJPA = mapperU.toJpa(ubicacion);
        ubiDaoSQL.save(ubiJPA);

        ubicacion.setId(ubiJPA.getId());
        ubicacion.setCreatedAt(ubiJPA.getCreatedAt());

        // Neo
        ubiDaoNeo.save(mapperU.toNeo(ubicacion));

        return ubicacion;
    }

    @Override
    public Ubicacion actualizar(Ubicacion ubicacion){
        // --- SQL ---
        UbicacionJPADTO ubiJPA = ubiDaoSQL.findById(ubicacion.getId())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new UbicacionNoEncontradaException(ubicacion.getId()));

        ubiJPA = mapperU.actualizarJPA(ubiJPA, ubicacion);
        ubiDaoSQL.save(ubiJPA);
        Ubicacion ubiActualizada = mapperU.toDomain(ubiJPA);

        // --- Neo4j ---
        ubiDaoNeo.save(mapperU.toNeo(ubiActualizada));
        return ubiActualizada;
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId){
        return ubiDaoSQL.findById(ubicacionId).filter(ubi -> !ubi.isDeleted()).map(mapperU::toDomain);
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
                || ubiDaoNeo.tieneConexiones(id)) {
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
        return mapperU.toDomainList(ubicaciones);
    }

    @Override
    public List<Cementerio> recuperarCementerios(){
        List<CementerioJPADTO> ubicaciones = ubiDaoSQL.recuperarCementerios();
        return mapperU.toDomainListCementerio(ubicaciones);
    }

    @Override
    public List<Santuario> recuperarSantuarios(){
        List<SantuarioJPADTO> ubicaciones = ubiDaoSQL.recuperarSantuarios();
        return mapperU.toDomainListSantuarios(ubicaciones);
    }

    @Override
    public Optional<Ubicacion> recuperarEliminado(Long id) {
        return this.ubiDaoSQL.recuperarEliminado(id).map(mapperU::toDomain);
    }

    @Override
    public List<Ubicacion> recuperarTodosEliminados() {
        return mapperU.toDomainList(this.ubiDaoSQL.recuperarTodosEliminados());
    }

    @Override
    public List<Espiritu> findEspiritusByUbicacionId(Long id){
        return mapperE.toDomainList(this.ubiDaoSQL.findEspiritusByUbicacionId(id));
    }

    @Override
    public List<Medium> findMediumsSinEspiritusByUbicacionId(Long id){
        return mapperM.toDomainList(ubiDaoSQL.findMediumsSinEspiritusByUbicacionId(id));
    }

    @Override
    public boolean estanConectadas(Long idOrigen,Long idDestino){
        return ubiDaoNeo.estanConectados(idOrigen,idDestino);
    }

    @Override
    public List<Ubicacion> caminoMasCortoEntre(Long idOrigen, Long idDestino) {
        List<UbicacionNeoDTO> nodosNeo = ubiDaoNeo.caminoMasCortoEntre(idOrigen, idDestino);

        return nodosNeo.stream()
                .map( neo -> ubiDaoSQL.findById(neo.getId())
                        .filter(u -> !u.isDeleted())
                        .orElseThrow(() -> new UbicacionNoEncontradaException(neo.getId()))
                )
                .map(mapperU::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        this.ubiDaoNeo.deleteAll();
        this.ubiDaoSQL.deleteAll();
    }

    @Override
    public List<ClosenessResult> closenessOf(List<Long> ids) {
        return ids.stream()
                .map(id -> new ClosenessResult(
                        mapper.toDomain(ubiDaoSQL.getReferenceById(id)),
                        ubiDaoNeo.closenessOf(id)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void conectar(Long idOrigen,Long idDestino){
        ubiDaoNeo.conectar(idOrigen, idDestino);
    }

    @Override
    public List<Santuario> obtenerSantuariosOrdenadosPorCorrupcion() {
        return mapperU.toDomainListSantuarios(ubiDaoSQL.obtenerSantuariosOrdenadosPorCorrupcion());
    }

    @Override
    public List<Medium> mediumConMayorDemoniacosEn(long ubicacionId) {
        return mapperM.toDomainList(ubiDaoSQL.mediumConMayorDemoniacosEn(ubicacionId));
    }

    @Override
    public int cantTotalDeDemoniacosEn(long ubicacionId) {
        return ubiDaoSQL.cantTotalDeDemoniacosEn(ubicacionId);
    }

    @Override
    public int cantTotalDeDemoniacosLibresEn(long ubicacionId) {
        return ubiDaoSQL.cantTotalDeDemoniacosLibresEn(ubicacionId);
    }

}
