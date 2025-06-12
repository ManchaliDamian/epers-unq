package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;
import java.util.Optional;


public interface EspirituRepository {
    Espiritu guardar(Espiritu espiritu, Coordenada coordenada);
    Espiritu actualizar(Espiritu espiritu);
    Espiritu actualizar(Espiritu espiritu, Coordenada coordenada);
    void actualizarUbicacionesPorMedium(Long mediumId, Ubicacion destino);
    void actualizarCoordenadasPorMedium(Long mediumId, GeoJsonPoint punto);
    void eliminarFisicoEnMongoSiExiste(Long id);
    List<Espiritu> recuperarTodos();
    Optional<Espiritu> recuperar(Long espirituId);
    Optional<Coordenada> recuperarCoordenada(Long espirituId);
    List<EspirituDemoniaco> recuperarDemonios();
    List<EspirituAngelical> recuperarAngeles();
    Optional<Espiritu> recuperarEliminado(Long id);
    List<Espiritu> recuperarTodosLosEliminados();
    List<EspirituAngelical> recuperarAngelesDe(Long mediumId);
    List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId);
    List<Espiritu> recuperarDemoniacosPaginados(Pageable pageable);
    void deleteAll();
}

