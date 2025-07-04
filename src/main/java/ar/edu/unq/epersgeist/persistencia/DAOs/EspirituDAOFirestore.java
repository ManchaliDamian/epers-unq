package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;

import java.util.List;

public interface EspirituDAOFirestore {
    void crear(Espiritu espiritu);
    Espiritu actualizar(Espiritu e);
    void eliminar(Long id);
    void enriquecer(Espiritu espiritu);
    void deleteAll();
    List<Espiritu> recuperarTodosMayorVida(int vida);
}
