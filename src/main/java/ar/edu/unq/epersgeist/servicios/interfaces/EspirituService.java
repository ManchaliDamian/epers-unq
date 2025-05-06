package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

import java.util.List;
import java.util.Optional;

public interface EspirituService {
    Espiritu guardar(Espiritu espiritu);
    void actualizar(Espiritu espiritu);
    Optional<Espiritu> recuperar(Long espirituId);
    List<Espiritu> recuperarTodos();
    void eliminar(Long espirituId);
    Medium conectar(Long espirituId, Long mediumId);
    List<Espiritu> espiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina);

}