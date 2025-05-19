package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import org.springframework.data.domain.PageRequest;
import ar.edu.unq.epersgeist.modelo.exception.NoHaySantuarioCorruptoException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final UbicacionDAOSQL ubicacionDAO;

    public EstadisticaServiceImpl(UbicacionDAOSQL ubicacionDAO){
        this.ubicacionDAO = ubicacionDAO;
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto(){
            Santuario santuarioMasCorrupto =
                ubicacionDAO
                        .obtenerSantuariosOrdenadosPorCorrupcion(PageRequest.of(0, 1))
                        .stream()
                        .findFirst()
                        .orElseThrow(NoHaySantuarioCorruptoException::new);

            long ubicacionId = santuarioMasCorrupto.getId();

        Medium mediumMayorCantDemoniacos = ubicacionDAO
                .mediumConMayorDemoniacosEn(ubicacionId)
                .stream()
                .findFirst()
                .orElse(null);

        int cantTotalDeDemoniacos = ubicacionDAO.cantTotalDeDemoniacosEn(ubicacionId);
            int cantTotalDeDemoniacosLibres = ubicacionDAO.cantTotalDeDemoniacosLibresEn(ubicacionId);
            return new ReporteSantuarioMasCorrupto(santuarioMasCorrupto.getNombre()
                                                   ,cantTotalDeDemoniacos
                                                   ,cantTotalDeDemoniacosLibres
                                                   ,mediumMayorCantDemoniacos);
    }

}
