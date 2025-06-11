package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.persistencia.DAOs.*;
import ar.edu.unq.epersgeist.persistencia.DTOs.estadistica.SnapshotMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EstadisticaRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EstadisticaRepositoryImp implements EstadisticaRepository {

    private MediumDAOSQL mediumDAOSQL;
    private MediumDAOMongo mediumDAOMongo;
    private EspirituDAOSQL espirituDAOSQL;
    private EspirituDAOMongo espirituDAOMongo;
    private UbicacionDAOSQL ubicacionDAOSQL;
    private PoligonoDAO poligonoDAO;
    private UbicacionDAONeo ubicacionDAONeo;

    public EstadisticaRepositoryImp(
            MediumDAOSQL mediumDAOSQL,
            MediumDAOMongo mediumDAOMongo,
            EspirituDAOSQL espirituDAOSQL,
            EspirituDAOMongo espirituDAOMongo,
            UbicacionDAOSQL ubicacionDAOSQL,
            PoligonoDAO poligonoDAO,
            UbicacionDAONeo ubicacionDAONeo
    ){
        this.mediumDAOSQL = mediumDAOSQL;
        this.mediumDAOMongo = mediumDAOMongo;
        this.espirituDAOSQL = espirituDAOSQL;
        this.espirituDAOMongo = espirituDAOMongo;
        this.ubicacionDAOSQL = ubicacionDAOSQL;
        this.poligonoDAO = poligonoDAO;
        this.ubicacionDAONeo = ubicacionDAONeo;
    }


    @Override
    public Date guardarSnapshot() {
        Map<String, Object> datosSql = this.crearSnapshotSQL();
        Map<String, Object> datosMongo = this.crearSnapshotMongo();
        Map<String, Object> datosNeo = this.crearSnapshotNeo();
        Date fechaDeCreacion = new Date();

        SnapshotMongoDTO snapshot = new SnapshotMongoDTO();
        snapshot.setFecha(fechaDeCreacion);
        snapshot.setSql(datosSql);
        snapshot.setMongo(datosMongo);
        snapshot.setNeo4j(datosNeo);
        
        // GUARDAR EN MONGO

        return fechaDeCreacion;
    }

    private Map<String, Object> crearSnapshotSQL(){
        Map<String, Object> datos = new HashMap<>();
        datos.put("mediums", this.mediumDAOSQL.findAll());
        datos.put("espiritus", this.espirituDAOSQL.findAll());
        datos.put("ubicaciones", this.ubicacionDAOSQL.findAll());
        return datos;
    }
    private Map<String, Object> crearSnapshotMongo(){
        Map<String, Object> datos = new HashMap<>();
        datos.put("mediums", this.mediumDAOMongo.findAll());
        datos.put("espiritus", this.espirituDAOMongo.findAll());
        datos.put("poligonos", this.poligonoDAO.findAll()); // ?????
        return datos;
    }
    private Map<String, Object> crearSnapshotNeo(){
        Map<String, Object> datos = new HashMap<>();
        datos.put("ubicaciones", this.ubicacionDAONeo.findAll());
        return datos;
    }

    @Override
    public void cargarSnapshot(Date fecha) {

    }
}
