package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.controller.dto.estadistica.SnapshotDTO;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import java.util.Date;

public interface EstadisticaService {
    ReporteSantuarioMasCorrupto santuarioCorrupto();
    void guardarSnapshot();
    SnapshotDTO cargarSnapshot(Date fechaDeCreacion);
}
