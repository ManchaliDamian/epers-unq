package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.CoordenadaMongoDTO;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

@Component("coordenadaMapperImpl")
public class CoordenadaMapperImpl implements CoordenadaMapper {

    public CoordenadaMongoDTO toMongo(Coordenada dominio, Long entidadId, TipoDeEntidad tipo) {
        // GeoJsonPoint recibe (x=longitud, y=latitud)
        GeoJsonPoint punto = new GeoJsonPoint(dominio.getLongitud(), dominio.getLatitud());
        return new CoordenadaMongoDTO(punto, entidadId, tipo);
    }

    public Coordenada toDomain(CoordenadaMongoDTO dto) {
        // dto.getPunto().getY() = latitud, getX() = longitud
        return new Coordenada(dto.getPunto().getY(), dto.getPunto().getX());
    }
}
