package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UbicacionRepository {
    Ubicacion guardar(Ubicacion ubicacion);
    Ubicacion actualizar(Ubicacion ubicacion);
    Optional<Ubicacion> recuperar(Long ubicacionId);
    void eliminar(Long id);
    List<Ubicacion> recuperarTodos();
    List<Cementerio> recuperarCementerios();
    List<Santuario> recuperarSantuarios();
    List<Espiritu> findEspiritusByUbicacionId(Long id);
    List<Medium> findMediumsSinEspiritusByUbicacionId(Long id);
    List<Medium> findMediumByUbicacionId(Long ubicacionId);
}
