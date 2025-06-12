package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.estadistica.SnapshotMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumMongoDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface SnapshotDAOMongo extends MongoRepository<SnapshotMongoDTO, String> {

    @Query("{ 'fecha' : ?0 }")
    Optional<SnapshotMongoDTO> findByFecha(java.util.Date fecha);

}
