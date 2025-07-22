package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.exception.Conflict.RecursoNoEliminable.UbicacionNoEliminableException;
import ar.edu.unq.epersgeist.exception.NotFound.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.persistencia.DAOs.UbicacionDAONeo;
import ar.edu.unq.epersgeist.persistencia.DAOs.UbicacionDAOSQL;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.CementerioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.SantuarioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.*;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;

import ar.edu.unq.epersgeist.servicios.interfaces.ClosenessResult;
import ar.edu.unq.epersgeist.servicios.interfaces.DegreeResult;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UbicacionRepositoryImpl implements UbicacionRepository {

    private UbicacionDAONeo ubiDAONeo;
    private UbicacionDAOSQL ubiDAOSQL;
    private UbicacionMapper mapperU;
    private MediumMapper mapperM;
    private EspirituMapper mapperE;
    private CentralityMapper mapperC;

    public UbicacionRepositoryImpl(UbicacionDAONeo ubiDAONeo, UbicacionDAOSQL ubiDAOSQL,
                                   MediumMapper mapperM, EspirituMapper mapperE,
                                   UbicacionMapper mapperU, CentralityMapper mapperC){
        this.ubiDAONeo = ubiDAONeo;
        this.ubiDAOSQL = ubiDAOSQL;
        this.mapperU = mapperU;
        this.mapperE = mapperE;
        this.mapperM = mapperM;
        this.mapperC = mapperC;
    }

    @Override
    public Ubicacion guardar(Ubicacion ubicacion){
        // SQL
        UbicacionJPADTO ubiJPA = mapperU.toJpa(ubicacion);
        ubiDAOSQL.save(ubiJPA);

        ubicacion.setId(ubiJPA.getId());
        ubicacion.setCreatedAt(ubiJPA.getCreatedAt());

        // Neo
        Optional<UbicacionNeoDTO> existente = ubiDAONeo.mergeByIdSQL(ubicacion.getId(), ubicacion.getNombre());
        UbicacionNeoDTO neoDto = existente.orElseGet(() -> mapperU.toNeo(ubicacion));
        ubiDAONeo.save(neoDto);

        return ubicacion;
    }

    @Override
    public Ubicacion actualizar(Ubicacion ubicacion){
        // --- SQL ---
        UbicacionJPADTO ubiJPA = ubiDAOSQL.findById(ubicacion.getId())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new UbicacionNoEncontradaException(ubicacion.getId()));

        ubiJPA = mapperU.actualizarJPA(ubiJPA, ubicacion);
        ubiDAOSQL.save(ubiJPA);
        Ubicacion ubiActualizada = mapperU.toDomain(ubiJPA);

        // --- Neo4j ---
        ubiDAONeo.save(mapperU.toNeo(ubiActualizada));
        return ubiActualizada;
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId){
        Optional<UbicacionJPADTO> recuperadoSql = this.recuperarSql(ubicacionId);
        Optional<UbicacionNeoDTO> recuperadoNeo = this.recuperarNeo(ubicacionId);

        Optional<Ubicacion> resultado = Optional.empty();
        if(recuperadoSql.isPresent() && recuperadoNeo.isPresent()){
            Ubicacion ubicacion = mapperU.toDomain(recuperadoSql.get());
            Set<Long> conexionesId = recuperadoNeo.get().getConexiones()
                    .stream().map(UbicacionNeoDTO::getIdSQL).collect(Collectors.toSet());
            List<UbicacionJPADTO> conexiones = ubiDAOSQL.findAllById(conexionesId);
            List<Ubicacion> conexionesUbicacion = mapperU.toDomainList(conexiones);
            ubicacion.setConexiones(new HashSet<>(conexionesUbicacion));
            resultado = Optional.of(ubicacion);
        }
        return resultado;
    }

    private Optional<UbicacionJPADTO> recuperarSql(Long ubicacionId){
        return ubiDAOSQL.findById(ubicacionId).filter(u -> !u.isDeleted());
    }


    private Optional<UbicacionNeoDTO> recuperarNeo(Long ubicacionId){
        return ubiDAONeo.findByIdSQL(ubicacionId);
    }

    @Override
    public void eliminar(Long id) {
        if (!ubiDAOSQL.findEspiritusByUbicacionId(id).isEmpty()
                || !ubiDAOSQL.findMediumByUbicacionId(id).isEmpty()
                || ubiDAONeo.tieneConexiones(id)) {
            throw new UbicacionNoEliminableException(id);
        }
        UbicacionJPADTO ubicacionSQLAEliminar = this.recuperarSql(id)
                .orElseThrow(() -> new UbicacionNoEncontradaException(id));
        ubicacionSQLAEliminar.setDeleted(true);

        ubiDAONeo.deleteByIdSQL(id); //hard delete
        ubiDAOSQL.save(ubicacionSQLAEliminar); //soft delete

    }

    @Override
    public List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino) {
        List<UbicacionNeoDTO> nodosNeo = ubiDAONeo.caminoMasCorto(idOrigen, idDestino);

        return nodosNeo.stream()
                .map( neo -> ubiDAOSQL.findById(neo.getIdSQL())
                        .orElseThrow(() -> new UbicacionNoEncontradaException(neo.getIdSQL()))
                )
                .map(mapperU::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {
        ubiDAONeo.deleteAll();
        ubiDAOSQL.deleteAll();
    }

    @Override
    public void conectar(Long idOrigen,Long idDestino){
        ubiDAONeo.conectar(idOrigen, idDestino);
    }

    @Override
    public List<Ubicacion> ubicacionesSobrecargadas(Integer umbralDeEnergia) {
        return mapperU.toDomainList(ubiDAOSQL.ubicacionesSobrecargadas(umbralDeEnergia));
    }

    @Override
    public List<Ubicacion> recuperarConexiones(Long ubicacionId) {
        return ubiDAONeo.recuperarConexiones(ubicacionId).stream()
                .map(vecinoNeo -> {
                    Long vecinoId = vecinoNeo.getIdSQL();
                    UbicacionJPADTO jpaDTO = ubiDAOSQL.findById(vecinoId)
                            .orElseThrow(() -> new UbicacionNoEncontradaException(vecinoId));
                    return mapperU.toDomain(jpaDTO);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Ubicacion> recuperarTodos(){
        List<UbicacionJPADTO> ubicaciones = ubiDAOSQL.recuperarTodos();
        return mapperU.toDomainList(ubicaciones);
    }

    @Override
    public List<Cementerio> recuperarCementerios(){
        List<CementerioJPADTO> ubicaciones = ubiDAOSQL.recuperarCementerios();
        return mapperU.toDomainListCementerio(ubicaciones);
    }

    @Override
    public List<Santuario> recuperarSantuarios(){
        List<SantuarioJPADTO> ubicaciones = ubiDAOSQL.recuperarSantuarios();
        return mapperU.toDomainListSantuarios(ubicaciones);
    }

    @Override
    public Optional<Ubicacion> recuperarEliminado(Long id) {
        return ubiDAOSQL.recuperarEliminado(id).map(mapperU::toDomain);
    }

    @Override
    public List<Ubicacion> recuperarTodosEliminados() {
        return mapperU.toDomainList(ubiDAOSQL.recuperarTodosEliminados());
    }

    @Override
    public List<Espiritu> findEspiritusByUbicacionId(Long id){
        return mapperE.toDomainList(ubiDAOSQL.findEspiritusByUbicacionId(id));
    }

    @Override
    public List<Medium> findMediumsSinEspiritusByUbicacionId(Long id){
        return mapperM.toDomainList(ubiDAOSQL.findMediumsSinEspiritusByUbicacionId(id));
    }

    @Override
    public boolean estanConectadas(Long idOrigen,Long idDestino){
        return ubiDAONeo.estanConectadas(idOrigen,idDestino);
    }

    @Override
    public List<Santuario> obtenerSantuariosOrdenadosPorCorrupcion() {
        return mapperU.toDomainListSantuarios(ubiDAOSQL.obtenerSantuariosOrdenadosPorCorrupcion());
    }

    @Override
    public List<Medium> mediumConMayorDemoniacosEn(long ubicacionId) {
        return mapperM.toDomainList(ubiDAOSQL.mediumConMayorDemoniacosEn(ubicacionId));
    }

    @Override
    public int cantTotalDeDemoniacosEn(long ubicacionId) {
        return ubiDAOSQL.cantTotalDeDemoniacosEn(ubicacionId);
    }

    @Override
    public int cantTotalDeDemoniacosLibresEn(long ubicacionId) {
        return ubiDAOSQL.cantTotalDeDemoniacosLibresEn(ubicacionId);
    }

    @Override
    public List<ClosenessResult> closenessOf(List<Long> ids) {
        return mapperC.toDomainListCloseness(ubiDAONeo.closenessOf(ids));
    }

    @Override
    public List<DegreeResult> degreeOf(List<Long> ids){
        return mapperC.toDomainListDegree(ubiDAONeo.degreeOf(ids));
    }

}
