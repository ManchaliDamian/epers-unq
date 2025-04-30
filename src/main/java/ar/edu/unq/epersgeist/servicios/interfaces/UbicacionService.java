package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.List;
import java.util.Optional;

public interface UbicacionService {
    Ubicacion guardar(Ubicacion ubicacion);
    Ubicacion actualizar(Ubicacion ubicacion);
    Optional<Ubicacion> recuperar(Long ubicacionId);
    List<Ubicacion> recuperarTodos();
    void eliminar(Ubicacion ubicacion);
    void eliminar(Long id);
    List<Espiritu> espiritusEn(Long ubicacionId);
    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);
    void clearAll();
}
