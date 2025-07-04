package ar.edu.unq.epersgeist.servicios.interfaces;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import java.util.List;
import java.util.Optional;

public interface PoligonoService {
    // CRUD
    void guardar(Long ubicacionId, Poligono poligono);
    Optional<Poligono> recuperar(String poligonoId);
    Optional<Poligono> recuperarPorUbicacionId(Long ubicacionId);
    List<Poligono> recuperarTodos();
    void eliminar(Long ubicacionId);
    void eliminarTodos();

}