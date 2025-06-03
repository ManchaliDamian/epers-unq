package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UbicacionDAOMongo extends MongoRepository<UbicacionMongoDTO, Long> {

}
