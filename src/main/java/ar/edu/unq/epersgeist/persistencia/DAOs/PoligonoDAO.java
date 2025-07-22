package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PoligonoDAO extends MongoRepository<PoligonoMongoDTO, String> {
    Optional<PoligonoMongoDTO> findByUbicacionId(Long ubicacionId);
    void deleteByUbicacionId(Long ubicacionId);

    // busca el polígono de una ubicación que *contenga* el punto
    @Query("{ 'poligono': { $geoIntersects: { $geometry: ?0 } }, 'ubicacionId': ?1 }")
    Optional<PoligonoMongoDTO> findByPoligonoGeoIntersectsAndUbicacionId(GeoJsonPoint punto, Long ubicacionId);

    @Query("{ 'poligono': { $geoIntersects: { $geometry: ?0 } }, 'ubicacionId': { $ne: ?1 } }")
    Optional<PoligonoMongoDTO> findOneIntersectandoPoligonoConOtraUbicacion(GeoJsonPolygon poligono, Long ubicacionId);

    @Aggregation(pipeline = {
            "{ $match: { 'poligono': { $geoIntersects: { $geometry: { type: 'Point', coordinates: [?1, ?0] } } } } }",
            "{ $project: { _id: 0, ubicacionId: 1 } }"
    })
    Optional<Long> ubicacionIdConCoordenadas(Double latitud, Double longitud);


    @Aggregation(pipeline = {
            "{ $match: { 'ubicacionId': ?0 } }",
            "{ $project: { _id: 0, coord: { $first: { $first: '$poligono.coordinates' } } } }",
            "{ $project: { latitud: { $arrayElemAt: ['$coord', 1] }, longitud: { $arrayElemAt: ['$coord', 0] } } }"
    })
    Optional<Coordenada> recuperarCoordenadaAleatoria(Long ubicacionId);

}