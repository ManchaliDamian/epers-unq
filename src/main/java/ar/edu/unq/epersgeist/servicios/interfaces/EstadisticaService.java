package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;

import java.util.Date;

public interface EstadisticaService {
    ReporteSantuarioMasCorrupto santuarioCorrupto();
    Date guardarSnapshot();
}
