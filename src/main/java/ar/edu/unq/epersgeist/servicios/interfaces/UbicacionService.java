package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import java.util.List;
import java.util.Optional;

public interface UbicacionService {
    Ubicacion guardar(Ubicacion ubicacion, Poligono poligono);
    Ubicacion actualizar(Ubicacion ubicacion);
    Optional<Ubicacion> recuperar(Long ubicacionId);
    List<Ubicacion> recuperarTodos();
    List<Cementerio> recuperarCementerios();
    List<Santuario> recuperarSantuarios();
    void eliminar(Long id);
    List<Espiritu> espiritusEn(Long ubicacionId);
    List<Medium> mediumsSinEspiritusEn(Long ubicacionId);
    boolean estanConectadas(Long idOrigen, Long idDestino);
    List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino);
    void conectar(Long idOrigen, Long idDestino);
    List<ClosenessResult> closenessOf(List<Long> ids);
    List<Ubicacion> ubicacionesSobrecargadas(Integer umbralDeEnergia);
    List<DegreeResult> degreeOf(List<Long> ids);
    List<Ubicacion> recuperarConexiones(Long ubicacionId);
}
