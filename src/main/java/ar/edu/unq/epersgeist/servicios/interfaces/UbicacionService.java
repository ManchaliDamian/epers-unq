package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Ubicacion;

import java.util.List;
import java.util.Optional;

public interface UbicacionService {
    Ubicacion guardar(Ubicacion ubicacion);
    Ubicacion actualizar(Ubicacion ubicacion);
    Optional<Ubicacion> recuperar(Long ubicacionId);
    List<Ubicacion> recuperarTodos();
    List<Cementerio> recuperarCementerios();
    List<Santuario> recuperarSantuarios();
    void eliminar(Long id);
    List<Espiritu> espiritusEn(Long ubicacionId);
    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);
    Optional<Ubicacion> recuperarEliminado(Long id);
    List<Ubicacion> recuperarTodosEliminados();
}
