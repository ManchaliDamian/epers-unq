package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;


import ar.edu.unq.epersgeist.modelo.Snapshot;

import java.time.LocalDate;
import java.util.Optional;


public interface EstadisticaRepository {
    void crearSnapshot();
    Optional<Snapshot> obtenerSnapshot(LocalDate fecha);
    void delete(Snapshot snapshot);
    void deleteAll();
}

