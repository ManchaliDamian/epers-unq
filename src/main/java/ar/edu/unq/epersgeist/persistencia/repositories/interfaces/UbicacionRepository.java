package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;

import java.util.List;
import java.util.Optional;

public interface UbicacionRepository {
    Ubicacion guardar(Ubicacion ubicacion);
    Ubicacion actualizar(Ubicacion ubicacion);
    Optional<Ubicacion> recuperar(Long ubicacionId);
    void eliminar(Long id);
    List<Ubicacion> recuperarTodos();
    List<Cementerio> recuperarCementerios();
    List<Santuario> recuperarSantuarios();
    Optional<Ubicacion> recuperarEliminado(Long id);
    List<Ubicacion> recuperarTodosEliminados();
    List<Espiritu> findEspiritusByUbicacionId(Long id);
    List<Medium> findMediumsSinEspiritusByUbicacionId(Long id);
    void deleteAll();
    boolean estanConectadas(Long idOrigen,Long idDestino);
    List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino);
    void conectar(Long idOrigen,Long idDestino);
    List<Ubicacion> ubicacionesSobrecargadas(Integer umbralDeEnergia);

    //deberian estar aca?
    List<Santuario> obtenerSantuariosOrdenadosPorCorrupcion();
    List<Medium> mediumConMayorDemoniacosEn(long ubicacionId);
    int cantTotalDeDemoniacosEn(long ubicacionId);
    int cantTotalDeDemoniacosLibresEn(long ubicacionId);
}
