package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;

import java.util.Optional;

public interface MediumDAOMongo extends MongoRepository<MediumMongoDTO, String> {

    @Aggregation(pipeline ={
            "{'$geoNear': { " +
                    "'near':{'type': 'Point', 'coordinates': [?0, ?1]}, " +
                    "'distanceField': 'distancia', " +
                    "'spherical': true, " +
                    "'query': { 'idSQL': ?2 }, " +
                    "'maxDistance': 50000 " +
                    "}} " +
            "{ $project: { 'distancia': 1, '_id': 0 }} "
        }
    )
    Double distanciaA(Double longitud, Double latitud, Long idMediumSQL);

    Optional<MediumMongoDTO> findByIdSQL(Long id);

    void deleteByIdSQL(Long espirituId);
}
