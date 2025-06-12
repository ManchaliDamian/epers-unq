package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspirituDAOMongo extends MongoRepository<EspirituMongoDTO, String> {
    @Query("{ 'idSQL' : ?0 }")
    Optional<EspirituMongoDTO> findByIdSQL(Long espirituId);

    void deleteByIdSQL(Long espirituId);

    @Modifying
    @Query("{ 'mediumConectadoIdSQL': ?0 }")
    @Update("{ '$set': { 'punto': ?1 } }")
    void actualizarCoordenadasPorMedium(Long mediumId, GeoJsonPoint nuevoPunto);
}
