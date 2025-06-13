package ar.edu.unq.epersgeist.servicios.impl;
import ar.edu.unq.epersgeist.controller.dto.estadistica.SnapshotDTO;
import ar.edu.unq.epersgeist.exception.NotFound.SnapshotNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Snapshot;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EstadisticaRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import ar.edu.unq.epersgeist.exception.NotFound.NoHaySantuarioCorruptoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final UbicacionRepository ubicacionRepository;
    private final EstadisticaRepository estadisticaRepository;

    public EstadisticaServiceImpl(
            UbicacionRepository ubicacionRepository,
            EstadisticaRepository estadisticaRepository
    ){
        this.ubicacionRepository = ubicacionRepository;
        this.estadisticaRepository = estadisticaRepository;
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


    // snapshot

    @Override
    public void crearSnapshot() {
        estadisticaRepository.crearSnapshot();
    }

    @Override
    public Snapshot obtenerSnapshot(LocalDate fecha){
        return estadisticaRepository.obtenerSnapshot(fecha)
                .orElseThrow(() -> new SnapshotNoEncontradoException(fecha));
    }

    @Override
    public void eliminar(Snapshot snapshot) {
        estadisticaRepository.delete(snapshot);
    }
}
