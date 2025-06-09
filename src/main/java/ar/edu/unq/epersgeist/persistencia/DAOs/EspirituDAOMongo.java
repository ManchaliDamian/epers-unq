package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EspirituDAOMongo extends MongoRepository<EspirituMongoDTO, String> {
    @Query("{ 'idSQL' : ?0 }")
    Optional<EspirituMongoDTO> findByIdSQL(Long espirituId);
}
