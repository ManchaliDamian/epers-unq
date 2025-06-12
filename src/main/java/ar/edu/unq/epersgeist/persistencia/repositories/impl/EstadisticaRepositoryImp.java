package ar.edu.unq.epersgeist.persistencia.repositories.impl;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;
import ar.edu.unq.epersgeist.persistencia.DTOs.estadistica.SnapshotMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EstadisticaRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstadisticaRepositoryImp implements EstadisticaRepository {

    private MediumDAOSQL mediumDAOSQL;
    private MediumDAOMongo mediumDAOMongo;
    private EspirituDAOSQL espirituDAOSQL;
    private EspirituDAOMongo espirituDAOMongo;
    private UbicacionDAOSQL ubicacionDAOSQL;
    private PoligonoDAO poligonoDAO;
    private UbicacionDAONeo ubicacionDAONeo;
    private SnapshotDAOMongo snapshotDAOMongo;

    public EstadisticaRepositoryImp(
            MediumDAOSQL mediumDAOSQL,
            MediumDAOMongo mediumDAOMongo,
            EspirituDAOSQL espirituDAOSQL,
            EspirituDAOMongo espirituDAOMongo,
            UbicacionDAOSQL ubicacionDAOSQL,
            PoligonoDAO poligonoDAO,
            UbicacionDAONeo ubicacionDAONeo,
            SnapshotDAOMongo snapshotDAOMongo
    ){
        this.mediumDAOSQL = mediumDAOSQL;
        this.mediumDAOMongo = mediumDAOMongo;
        this.espirituDAOSQL = espirituDAOSQL;
        this.espirituDAOMongo = espirituDAOMongo;
        this.ubicacionDAOSQL = ubicacionDAOSQL;
        this.poligonoDAO = poligonoDAO;
        this.ubicacionDAONeo = ubicacionDAONeo;
        this.snapshotDAOMongo = snapshotDAOMongo;
    }


    @Override
    public void guardarSnapshot() {
        Date fechaDeCreacion = new Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        String fechaFormateada = sdf.format(fechaDeCreacion);

        SnapshotMongoDTO existente = this.snapshotDAOMongo.findAll().stream()
            .filter(s -> sdf.format(s.getFecha()).equals(fechaFormateada))
            .findFirst()
            .orElse(null);

        Map<String, Object> datosSql = this.crearSnapshotSQL();
        Map<String, Object> datosMongo = this.crearSnapshotMongo();
        Map<String, Object> datosNeo = this.crearSnapshotNeo();

        SnapshotMongoDTO snapshot;
        if (existente != null) {
            snapshot = existente;
        } else {
            snapshot = new SnapshotMongoDTO();
            snapshot.setFecha(fechaDeCreacion);
        }
        snapshot.setSql(datosSql);
        snapshot.setMongo(datosMongo);
        snapshot.setNeo4j(datosNeo);

        this.snapshotDAOMongo.save(snapshot);
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
    public void recuperarSnapshot(Date fecha) {
        SnapshotMongoDTO snapshot = this.snapshotDAOMongo.findByFecha(fecha).get();

        if (snapshot == null) {
            throw new IllegalArgumentException("No existe un snapshot con la fecha: " + fecha);
        }

        this.mediumDAOSQL.deleteAll();
        this.mediumDAOMongo.deleteAll();
        this.espirituDAOSQL.deleteAll();
        this.espirituDAOMongo.deleteAll();
        this.ubicacionDAOSQL.deleteAll();
        this.poligonoDAO.deleteAll();
        this.ubicacionDAONeo.deleteAll();

        Map<String, Object> sql = snapshot.getSql();
        this.mediumDAOSQL.saveAll((List<MediumJPADTO>) sql.get("mediums"));
        this.espirituDAOSQL.saveAll((List<EspirituJPADTO>) sql.get("espiritus"));
        this.ubicacionDAOSQL.saveAll((List<UbicacionJPADTO>) sql.get("ubicaciones"));

        Map<String, Object> mongo = snapshot.getMongo();
        this.mediumDAOMongo.saveAll((List<MediumMongoDTO>) mongo.get("mediums"));
        this.espirituDAOMongo.saveAll((List<EspirituMongoDTO>) mongo.get("espiritus"));
        this.poligonoDAO.saveAll((List<PoligonoMongoDTO>) mongo.get("poligonos"));

        Map<String, Object> neo4j = snapshot.getNeo4j();
        this.ubicacionDAONeo.saveAll((List<UbicacionNeoDTO>) neo4j.get("ubicaciones"));
    }
}
