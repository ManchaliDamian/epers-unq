package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.CoordenadaMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoordenadaDAO extends MongoRepository<CoordenadaMongoDTO, String> {
    //encontrar la coordenada donde esta la entidad con id
    Optional<CoordenadaMongoDTO> findByEntidadIdAndTipoDeEntidad(Long entidadId, TipoDeEntidad tipo);

}
