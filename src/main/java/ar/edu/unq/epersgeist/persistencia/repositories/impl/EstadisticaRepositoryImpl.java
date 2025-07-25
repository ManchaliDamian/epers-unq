package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Snapshot;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;
import ar.edu.unq.epersgeist.persistencia.DTOs.estadistica.SnapshotMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EstadisticaRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.*;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.*;

@Repository
public class EstadisticaRepositoryImpl implements EstadisticaRepository {

    private MediumDAOSQL mediumDAOSQL;
    private MediumDAOMongo mediumDAOMongo;
    private EspirituDAOSQL espirituDAOSQL;
    private EspirituDAOMongo espirituDAOMongo;
    private UbicacionDAOSQL ubicacionDAOSQL;
    private PoligonoDAO poligonoDAO;
    private UbicacionDAONeo ubicacionDAONeo;
    private SnapshotDAOMongo snapshotDAOMongo;
    private SnapshotMapper snapshotMapper;
    private MediumMapper mediumMapper;
    private EspirituMapper espirituMapper;
    private UbicacionMapper ubicacionMapper;

    public EstadisticaRepositoryImpl(
            MediumDAOSQL mediumDAOSQL,
            MediumDAOMongo mediumDAOMongo,
            EspirituDAOSQL espirituDAOSQL,
            EspirituDAOMongo espirituDAOMongo,
            UbicacionDAOSQL ubicacionDAOSQL,
            PoligonoDAO poligonoDAO,
            UbicacionDAONeo ubicacionDAONeo,
            SnapshotDAOMongo snapshotDAOMongo,
            SnapshotMapper snapshotMapper,
            MediumMapper mediumMapper,
            EspirituMapper espirituMapper,
            UbicacionMapper ubicacionMapper
    ){
        this.mediumDAOSQL = mediumDAOSQL;
        this.mediumDAOMongo = mediumDAOMongo;
        this.espirituDAOSQL = espirituDAOSQL;
        this.espirituDAOMongo = espirituDAOMongo;
        this.ubicacionDAOSQL = ubicacionDAOSQL;
        this.poligonoDAO = poligonoDAO;
        this.ubicacionDAONeo = ubicacionDAONeo;
        this.snapshotDAOMongo = snapshotDAOMongo;
        this.snapshotMapper = snapshotMapper;
        this.mediumMapper = mediumMapper;
        this.espirituMapper = espirituMapper;
        this.ubicacionMapper = ubicacionMapper;
    }


    @Override
    public void crearSnapshot() {

        // obtener la fecha actual sin hora
        LocalDate fechaFormateada = LocalDate.now();
        this.snapshotDAOMongo.deleteByFecha(fechaFormateada); // se borra fecha anterior

        // agarrar los datos de cada bdd
        Map<String, Object> datosSql = this.crearSnapshotSQL();
        Map<String, Object> datosMongo = this.crearSnapshotMongo();
        Map<String, Object> datosNeo = this.crearSnapshotNeo();

        // crear el snapshot
        Snapshot snapshot = new Snapshot(datosSql, datosMongo, datosNeo, fechaFormateada);
        SnapshotMongoDTO snapshotMongoDTO = snapshotMapper.toMongo(snapshot);

        // guardarlo
        this.snapshotDAOMongo.save(snapshotMongoDTO);
    }

    private Map<String, Object> crearSnapshotSQL(){
        Map<String, Object> datos = new HashMap<>();
        datos.put("mediums", this.mediumMapper.toDomainList(this.mediumDAOSQL.findAll()).stream().map(MediumDTO::desdeModelo).toList());
        datos.put("espiritus", this.espirituMapper.toDomainList(this.espirituDAOSQL.findAll()).stream().map(EspirituDTO::desdeModelo).toList());
        datos.put("ubicaciones", this.ubicacionMapper.toDomainList(this.ubicacionDAOSQL.findAll()).stream().map(UbicacionDTO::desdeModelo).toList());
        return datos;
    }

    private Map<String, Object> crearSnapshotMongo(){
        Map<String, Object> datos = new HashMap<>();
        datos.put("mediums", this.mediumDAOMongo.findAll());
        datos.put("espiritus", this.espirituDAOMongo.findAll());
        datos.put("poligonos", this.poligonoDAO.findAll());
        return datos;
    }

    private Map<String, Object> crearSnapshotNeo(){
        Map<String, Object> datos = new HashMap<>();
        datos.put("ubicaciones", this.ubicacionDAONeo.findAll());
        return datos;
    }

    @Override
    public Optional<Snapshot> obtenerSnapshot(LocalDate fecha) {
        return this.snapshotDAOMongo.findByFecha(fecha).map(snapshotMapper::toDomain);
    }

    @Override
    public void delete(Snapshot snapshot) {
        SnapshotMongoDTO snapshotMongoDTO = snapshotMapper.toMongo(snapshot);
        this.snapshotDAOMongo.delete(snapshotMongoDTO);
    }

    @Override
    public void deleteAll() {
        this.snapshotDAOMongo.deleteAll();
    }

}
