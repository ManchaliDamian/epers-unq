package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.enums.Direccion;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;

import java.util.List;
import java.util.Optional;

public interface EspirituService {
    Espiritu guardar(Espiritu espiritu, Coordenada coordenada);
    Espiritu actualizar(Espiritu espiritu);
    Espiritu actualizar(Espiritu espiritu, Coordenada coordenada);
    Optional<Espiritu> recuperar(Long espirituId);
    void eliminar(Long espirituId);
    Medium conectar(Long espirituId, Long mediumId);
    List<Espiritu> espiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina);
    List<Espiritu> recuperarTodos();
    List<EspirituAngelical> recuperarAngeles();
    List<EspirituDemoniaco> recuperarDemonios();


    void dominar(Long idEspiritu, Long idEspirituADominar);

    void combatir(Long idEspiritu, Long idEspirituACombatir);
    void desplazar(Long espirituId, Long ubicacionId);
}