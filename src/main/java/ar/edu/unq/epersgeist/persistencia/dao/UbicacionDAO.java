package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.List;

public interface UbicacionDAO {
    // continuar
    void guardar(Ubicacion ubicacion);
    Ubicacion recuperar(Long ubicacionId);
    void eliminar(Ubicacion ubicacion);
    void actualizar(Long ubicacionId, String nombreNuevo);

    List<Ubicacion> recuperarTodos();
}
