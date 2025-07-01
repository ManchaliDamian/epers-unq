package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;

import java.util.concurrent.ExecutionException;

public interface EspirituStatsFirebaseDAO {
    void save(Espiritu espiritu) throws InterruptedException, ExecutionException;
    Espiritu actualizar(Espiritu e);
    void eliminar(Long id);
    void enriquecer(Espiritu espiritu);
    void deleteAll();
}
