package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.CoordenadaMongoDTO;

public interface CoordenadaMapper {
        CoordenadaMongoDTO toMongo(Coordenada dominio, Long entidadId, TipoDeEntidad tipo);
        Coordenada toDomain(CoordenadaMongoDTO dto);
}

