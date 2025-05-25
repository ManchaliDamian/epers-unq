package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface EspirituRepository {
    Espiritu save(Espiritu espiritu);
    List<Espiritu> recuperarTodos();
    Optional<Espiritu> recuperar(Long espirituId);
    List<EspirituDemoniaco> recuperarDemonios();
    List<EspirituAngelical> recuperarAngeles();
    Optional<Espiritu> recuperarEliminado(Long id);
    List<Espiritu> recuperarTodosLosEliminados();
    List<EspirituAngelical> recuperarAngelesDe(Long mediumId);
    List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId);
    List<Espiritu> recuperarDemoniacosPaginados(Pageable pageable);
    void deleteAll();
}

