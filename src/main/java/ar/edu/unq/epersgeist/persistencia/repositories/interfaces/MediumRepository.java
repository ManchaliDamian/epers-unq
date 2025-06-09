package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;

import java.util.List;
import java.util.Optional;

public interface MediumRepository {
    Medium guardar(Medium medium, Coordenada coordenada);
    Medium actualizar(Medium medium);
    Medium actualizar(Medium medium, Coordenada coordenada);
    void eliminar(Long id);
    Optional<Medium> recuperar(Long mediumId);
    List<Medium> recuperarTodos();
    Optional<Medium> recuperarEliminado(Long id);
    List<Medium> recuperarTodosLosEliminados();
    List<Espiritu> findEspiritusByMediumId(Long mediumId);
    void deleteAll();
    Double laDistanciaA(Double longitud, Double latitud, Long idMediumSQL);
}
