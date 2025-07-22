package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import java.util.List;
import java.util.Optional;

public interface PoligonoRepository {
    void guardar(Long ubicacionId, Poligono poligono);
    List<Poligono> recuperarTodos();
    Optional<Poligono> recuperar(String poligonoId);
    Optional<Poligono> recuperarPorUbicacionId(Long ubicacionId);
    Optional<Long> ubicacionIdConCoordenadas(Double latitud, Double longitud);
    void eliminarPorUbicacionId(Long ubicacionId);
    void deleteAll();
    Optional<Coordenada> recuperarCoordenadaAleatoria(Long ubicacionId);
}