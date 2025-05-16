package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.enums.Direccion;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;

import java.util.List;
import java.util.Optional;

public interface EspirituService {
    Espiritu guardar(Espiritu espiritu);
    Espiritu actualizar(Espiritu espiritu);
    Optional<Espiritu> recuperar(Long espirituId);
    void eliminar(Long espirituId);
    Medium conectar(Long espirituId, Long mediumId);
    List<Espiritu> espiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina);
    List<Espiritu> recuperarTodosLosEliminados();
    Optional<Espiritu> recuperarEliminado(Long espirituId);
    List<Espiritu> recuperarTodos();
    List<EspirituAngelical> recuperarAngeles();
    List<EspirituDemoniaco> recuperarDemonios();
}