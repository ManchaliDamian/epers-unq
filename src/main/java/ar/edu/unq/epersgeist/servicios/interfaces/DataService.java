package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;


import java.util.List;
import java.util.Optional;

public interface DataService {
    void eliminarTodo();

    Optional<Ubicacion> recuperarEliminadoUbicacion(Long id);
    List<Ubicacion> recuperarTodosEliminadosDeUbicacion();
    Optional<Medium> recuperarEliminadoMedium(Long mediumId);
    List<Medium> recuperarTodosMediumsEliminados();
    Optional<Espiritu> recuperarEliminadoEspiritu(Long id);
    List<Espiritu> recuperarTodosLosEspiritusEliminados();
    List<Espiritu> crearYGuardarEspiritusAngelicales(int cantidad, Ubicacion ubi, Coordenada cc);
    List<Espiritu> recuperarTodosMayorVida(int vida);
    List<Espiritu> crearYGuardarEspiritusDemoniacos(int cantidad, Ubicacion ubi, Coordenada cc);
}
