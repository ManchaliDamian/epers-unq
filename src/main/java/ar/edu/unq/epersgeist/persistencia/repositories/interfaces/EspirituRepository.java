package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspirituRepository {
    Espiritu save(Espiritu espiritu);
    List<Espiritu> recuperarTodos();
    Optional<Espiritu> findById(Long espirituId);
    List<EspirituDemoniaco> recuperarDemonios();
    List<EspirituAngelical> recuperarAngeles();
    Optional<Espiritu> recuperarEliminado(Long id);
    List<Espiritu> recuperarTodosLosEliminados();
    List<EspirituAngelical> recuperarAngelesDe(Long mediumId);
    List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId);
    List<Espiritu> recuperarDemoniacosPaginados(Pageable pageable);
}

