package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;

import java.util.Date;
import java.util.Optional;

public interface EstadisticaService {
    ReporteSantuarioMasCorrupto santuarioCorrupto();
    void guardarSnapshot();
    void cargarSnapshot(Date fechaDeCreacion);
}
