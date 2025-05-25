package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;

import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import ar.edu.unq.epersgeist.modelo.exception.NoHaySantuarioCorruptoException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final UbicacionRepository ubicacionRepository;

    public EstadisticaServiceImpl(UbicacionRepository ubicacionRepository){
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto(){
            Santuario santuarioMasCorrupto =
                    ubicacionRepository
                        .obtenerSantuariosOrdenadosPorCorrupcion()
                        .stream()
                        .findFirst()
                        .orElseThrow(NoHaySantuarioCorruptoException::new);

            long ubicacionId = santuarioMasCorrupto.getId();

        Medium mediumMayorCantDemoniacos = ubicacionRepository
                .mediumConMayorDemoniacosEn(ubicacionId)
                .stream()
                .findFirst()
                .orElse(null);

        int cantTotalDeDemoniacos = ubicacionRepository.cantTotalDeDemoniacosEn(ubicacionId);
            int cantTotalDeDemoniacosLibres = ubicacionRepository.cantTotalDeDemoniacosLibresEn(ubicacionId);
            return new ReporteSantuarioMasCorrupto(santuarioMasCorrupto.getNombre()
                                                   ,cantTotalDeDemoniacos
                                                   ,cantTotalDeDemoniacosLibres
                                                   ,mediumMayorCantDemoniacos);
    }

}
