package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediumService {
    Medium crear(Medium unMedium);
    Optional<Medium> recuperar(Long mediumId);
    List<Medium> recuperarTodos();
    void actualizar(Medium unMedium);
    void eliminar(Long mediumId);
    void descansar(Long mediumId);
    void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar);
    List<Espiritu> espiritus(Long mediumId);
    Espiritu invocar(Long mediumId, Long espirituId);
}
