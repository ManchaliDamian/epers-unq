package ar.edu.unq.epersgeist.persistencia.repositories.interfaces;

import ar.edu.unq.epersgeist.controller.dto.estadistica.SnapshotDTO;
import java.util.Date;


public interface EstadisticaRepository {
    void guardarSnapshot();
    SnapshotDTO recuperarSnapshot(Date fecha);
}

