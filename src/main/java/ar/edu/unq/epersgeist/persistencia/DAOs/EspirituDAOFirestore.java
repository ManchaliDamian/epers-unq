package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituFirestoreDTO;

import java.util.concurrent.ExecutionException;

public interface EspirituDAOFirestore {
    void save(EspirituFirestoreDTO espiritu) throws InterruptedException, ExecutionException;
    EspirituFirestoreDTO actualizar(EspirituFirestoreDTO e);
    void eliminar(Long id);
    void enriquecer(Espiritu espiritu);
    void deleteAll();
}
