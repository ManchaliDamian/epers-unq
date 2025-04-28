package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UbicacionDAO extends JpaRepository<Ubicacion, Long> {

    @Query("from Espiritu e where e.ubicacion.id = :ubicacionId")
    List<Espiritu> findEspiritusByUbicacionId(Long ubicacionId);

    @Query("from Medium m where m.ubicacion.id = :ubicacionId and size(m.espiritus) = 0")
    List<Medium> findMediumsSinEspiritusByUbicacionId(Long ubicacionId);

}
