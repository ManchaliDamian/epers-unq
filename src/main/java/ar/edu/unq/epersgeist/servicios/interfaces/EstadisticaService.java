package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import org.springframework.stereotype.Repository;

public interface EstadisticaService {
    ReporteSantuarioMasCorrupto santuarioCorrupto();
}
