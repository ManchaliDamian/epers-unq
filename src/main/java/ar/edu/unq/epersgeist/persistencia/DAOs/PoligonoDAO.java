package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PoligonoDAO extends MongoRepository<PoligonoMongoDTO, String> {
    Optional<PoligonoMongoDTO> findByUbicacionId(Long ubicacionId);
    void deleteByUbicacionId(Long ubicacionId);

    // busca el polígono de una ubicación que *contenga* el punto
    @Query("{ 'poligono': { $geoIntersects: { $geometry: ?0 } } }")
    Optional<PoligonoMongoDTO> findByPoligonoGeoIntersects(GeoJsonPoint punto);


    @Query("{ 'poligono': { $geoIntersects: { $geometry: ?0 } }, 'ubicacionId': { $ne: ?1 } }")
    Optional<PoligonoMongoDTO> findOneIntersectandoPuntoEnOtraUbicacion(GeoJsonPoint punto, Long ubicacionId);

}