package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.controller.dto.estadistica.SnapshotDTO;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.persistencia.DAOs.SnapshotDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DTOs.estadistica.SnapshotMongoDTO;

import java.util.Date;
import java.util.Optional;

public interface EstadisticaService {
    ReporteSantuarioMasCorrupto santuarioCorrupto();
    void guardarSnapshot();
    SnapshotDTO cargarSnapshot(Date fechaDeCreacion);
}
