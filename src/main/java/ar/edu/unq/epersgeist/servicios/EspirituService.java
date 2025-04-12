package ar.edu.unq.epersgeist.servicios;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;

public interface EspirituService {
    void guardar(Espiritu espiritu);
    Espiritu recuperar(Long espirituId);
    List<Espiritu> recuperarTodos();
    void actualizar(Espiritu espiritu);
    void eliminar(Long espirituId);
    void eliminarTodo();
    Medium conectar(Long espirituId, Long mediumId);
    List<Espiritu> espiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina);

}