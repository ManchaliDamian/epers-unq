package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PoligonoDAO extends MongoRepository<PoligonoMongoDTO, String> {
    Optional<PoligonoMongoDTO> findByUbicacionId(Long ubicacionId);
    void deleteByUbicacionId(Long ubicacionId);
}