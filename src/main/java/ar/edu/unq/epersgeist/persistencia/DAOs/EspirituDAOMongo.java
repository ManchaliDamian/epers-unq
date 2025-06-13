package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EspirituDAOMongo extends MongoRepository<EspirituMongoDTO, String> {

    @Aggregation(pipeline = {
            "{ '$geoNear': { " +
                    "'near': { 'type': 'Point', 'coordinates': [?0, ?1] }, " +
                    "'distanceField': 'distancia', " +
                    "'spherical': true, " +
                    "'query': { 'idSQL': ?2 }, " +
                    "'maxDistance': 5000 " +
                    "} }",
            "{ '$match': { 'distancia': { '$gte': 2000, '$lte': 5000 } } }",
            "{ '$project': { 'distancia': 1, '_id': 0 } }"
    })
    Optional<Double> distanciaA(Double longitud, Double latitud, Long idEspirituSQL);

    @Query("{ 'idSQL' : ?0 }")
    Optional<EspirituMongoDTO> findByIdSQL(Long espirituId);

    void deleteByIdSQL(Long espirituId);

    @Modifying
    @Query("{ 'mediumConectadoIdSQL': ?0 }")
    @Update("{ '$set': { 'punto': ?1 } }")
    void actualizarCoordenadasPorMedium(Long mediumId, GeoJsonPoint nuevoPunto);
}
