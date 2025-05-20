package ar.edu.unq.epersgeist.persistencia.DAOs;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumJPADTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediumDAO extends JpaRepository<MediumJPADTO, Long> {
    @Query(
            "FROM Medium m where m.deleted = false"
    )
    List<Medium> recuperarTodos();
    @Query(
            "FROM Medium m where m.deleted = true and m.id = :id"
    )
    Optional<Medium> recuperarEliminado(@Param("id") Long id);
    @Query(
            "FROM Medium m where m.deleted = true"
    )
    List<Medium> recuperarTodosEliminados();

    @Query("SELECT e FROM Espiritu e WHERE e.mediumConectado.id = :mediumId " +
            "and e.deleted = false and e.mediumConectado.deleted = false")
    List<Espiritu> findEspiritusByMediumId(@Param("mediumId") Long mediumId);
}