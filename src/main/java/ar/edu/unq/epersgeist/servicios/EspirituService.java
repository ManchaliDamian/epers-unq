package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.List;

public interface EspirituService {
    void guardar(Espiritu espiritu);
    Espiritu recuperar(Long espirituId);
    List<Espiritu> recuperarTodos();
    void actualizar(Espiritu espiritu);
    void eliminar(Long espirituId);
    void eliminarTodo();
    Medium conectar(Long espirituId, Long mediumId);
    List<Espiritu> espiritusDemoniacos();
    List<Espiritu> recuperarPaginados(int page, int pageSize);
}