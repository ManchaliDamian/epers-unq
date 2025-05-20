package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MediumRepository {
    Medium save(Medium medium);
    List<Medium> recuperarTodos();
    Optional<Medium> recuperarEliminado(@Param("id") Long id);
    List<Medium> recuperarTodosEliminados();
    List<Espiritu> findEspiritusByMediumId(Long mediumId);
}
