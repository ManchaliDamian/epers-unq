package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.exception.NoHaySantuariosRegistradosException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final UbicacionDAO ubicacionDAO;

    public EstadisticaServiceImpl(UbicacionDAO ubicacionDAO){
        this.ubicacionDAO = ubicacionDAO;
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto(){
            Santuario santuarioMasCorrupto =
                ubicacionDAO
                        .obtenerSantuariosOrdenadosPorCorrupcion(PageRequest.of(0, 1))
                        .stream()
                        .findFirst()
                        .orElseThrow(NoHaySantuariosRegistradosException::new);

            long ubicacionId = santuarioMasCorrupto.getId();

            Medium mediumMayorCantDemoniacos =
                    ubicacionDAO
                            .mediumConMayorDemoniacosEn(ubicacionId, PageRequest.of(0, 1))
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
