package ar.edu.unq.epersgeist.persistencia.repositorys.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;

import java.util.List;
import java.util.Optional;

public interface UbicacionRepository {
    Ubicacion guardar(Ubicacion ubicacion);
    Ubicacion actualizar(Ubicacion ubicacion);
    Optional<Ubicacion> recuperar(Long ubicacionId);
    Optional<UbicacionJPA> recuperarSql(Long ubicacionId);
    List<Ubicacion> recuperarTodos();
    List<Cementerio> recuperarCementerios();
    List<Santuario> recuperarSantuarios();
    Optional<Ubicacion> recuperarEliminado(Long id);
    List<Ubicacion> recuperarTodosEliminados();
    List<Espiritu> findEspiritusByUbicacionId(Long id);
    List<Medium> findMediumsSinEspiritusByUbicacionId(Long id);

}
