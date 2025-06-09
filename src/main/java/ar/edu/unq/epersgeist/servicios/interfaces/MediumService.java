package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;

import java.util.List;
import java.util.Optional;

public interface MediumService {
    Medium guardar(Medium unMedium, Coordenada coordenada);
    Medium actualizar(Medium unMedium);
    Optional<Medium> recuperar(Long mediumId);
    List<Medium> recuperarTodos();
    void eliminar(Long mediumId);
    void descansar(Long mediumId);
    void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar);
    List<Espiritu> espiritus(Long mediumId);
    List<EspirituAngelical> angeles(Long mediumId);
    List<EspirituDemoniaco> demonios(Long mediumId);
    Espiritu invocar(Long mediumId, Long espirituId);
    void mover(Long mediumId, Long ubicacionId);

}
