package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import java.util.Optional;

public interface MediumDAOMongo extends MongoRepository<MediumMongoDTO, String> {



    @Aggregation(pipeline = {
            "{'$geoNear': { " +
                    "'near': {'type': 'Point', 'coordinates': [?1, ?0]}, " +
                    "'distanceField': 'distancia', " +
                    "'spherical': true, " +
                    "'query': { 'idSQL': ?2 } " +
                    "}}",
            "{ $project: { 'distancia': { $divide: ['$distancia', 1000] }, '_id': 0 }}"
    })
    Optional<Double> distanciaA(Double latitud, Double longitud, Long idMediumSQL);
    Optional<MediumMongoDTO> findByIdSQL(Long id);

    void deleteByIdSQL(Long espirituId);
}
