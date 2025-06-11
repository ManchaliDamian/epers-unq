package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;

import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOSQL;
import ar.edu.unq.epersgeist.persistencia.DAOs.MediumDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DAOs.MediumDAOSQL;
import ar.edu.unq.epersgeist.persistencia.DTOs.estadistica.SnapshotMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EstadisticaRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.MediumMapper;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import ar.edu.unq.epersgeist.exception.NoHaySantuarioCorruptoException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private final UbicacionRepository ubicacionRepository;
    private final EstadisticaRepository estadisticaRepository;

    public EstadisticaServiceImpl(
            UbicacionRepository ubicacionRepository,
            EstadisticaRepository estadisticaRepository,
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

    public Date guardarSnapshot() {
        return estadisticaRepository.guardarSnapshot();
    }

    public void cargarSnapshot(Date fecha){
        // Devolver 404 o un objeto vacio
        SnapshotMongoDTO snapshot = estadisticaRepository.recuperarSnapshot(fecha);

        // vaciar sql {}
        snapshot.getSql().forEach(entity -> {
            entity
        });
        // vaciar mongo
        // vaciar neo

    }
}
