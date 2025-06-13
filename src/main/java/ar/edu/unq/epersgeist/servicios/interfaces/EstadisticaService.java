package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.controller.dto.estadistica.SnapshotDTO;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Snapshot;

import java.time.LocalDate;
import java.util.Date;

public interface EstadisticaService {
    ReporteSantuarioMasCorrupto santuarioCorrupto();
    void crearSnapshot();
    Snapshot obtenerSnapshot(LocalDate fecha);
    void eliminar(Snapshot snapshot);
}
