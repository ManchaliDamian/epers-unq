package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public interface PoligonoMapper {
    PoligonoMongoDTO toMongo(Long ubicacionId, Poligono poligono);
    Poligono toDomain(PoligonoMongoDTO poligonoMongoDTO);
}
