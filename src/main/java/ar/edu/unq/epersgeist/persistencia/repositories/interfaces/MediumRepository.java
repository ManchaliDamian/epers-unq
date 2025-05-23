package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface MediumRepository {
    Medium save(Medium medium);
    Optional<Medium> recuperar(Long mediumId);
    List<Medium> recuperarTodos();
    Optional<Medium> recuperarEliminado(Long id);
    List<Medium> recuperarTodosLosEliminados();
    List<Espiritu> findEspiritusByMediumId(Long mediumId);
    void deleteAll();
}
