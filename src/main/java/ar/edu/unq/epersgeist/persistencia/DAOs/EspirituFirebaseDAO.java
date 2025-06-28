package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;

import java.util.concurrent.ExecutionException;

public interface EspirituFirebaseDAO {
    void save(Espiritu espiritu) throws InterruptedException, ExecutionException;
}
