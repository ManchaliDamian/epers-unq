package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private UbicacionDAO ubicacionDAO;

    public EstadisticaServiceImpl(UbicacionDAO ubicacionDAO){
        this.ubicacionDAO = ubicacionDAO;
    }

    public ReporteSantuarioMasCorrupto santuarioCorrupto(){
            Santuario santuarioMasCorrupto = ubicacionDAO.obtenerSantuarioMasCorrupto();
            long ubicacionId = santuarioMasCorrupto.getId();
            Medium mediumMayorCantDemoniacos = ubicacionDAO.mediumConMayorDemoniacosEn(ubicacionId);
            int cantTotalDeDemoniacos = ubicacionDAO.cantTotalDeDemoniacosEn(ubicacionId);
            int cantTotalDeDemoniacosLibres = ubicacionDAO.cantTotalDeDemoniacosLibresEn(ubicacionId);
            return new ReporteSantuarioMasCorrupto(santuarioMasCorrupto.getNombre()
                                                   ,cantTotalDeDemoniacos
                                                   ,cantTotalDeDemoniacosLibres
                                                   ,mediumMayorCantDemoniacos);
    }

}
