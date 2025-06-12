package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.ReporteSantuarioCorruptoDTO;
import ar.edu.unq.epersgeist.exception.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.exception.SnapshotNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.persistencia.DTOs.estadistica.SnapshotMongoDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/estadistica")
public final class EstadisticaControllerREST {

    private final EstadisticaService estadisticaService;

    public EstadisticaControllerREST(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping
    public ResponseEntity<ReporteSantuarioCorruptoDTO> obtenerSantuarioMasCorrupto() {
        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();

        ReporteSantuarioCorruptoDTO dto = ReporteSantuarioCorruptoDTO.desdeModelo(reporte);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/snapshot/create")
    public ResponseEntity<String> crearSnapshot(){
        estadisticaService.guardarSnapshot();
        return ResponseEntity.ok("Snapshot tomada con éxito");
    }

    @PostMapping("/snapshot/load/{fechaDeCreacion}")
    public ResponseEntity<String> cargarSnapshot(@PathVariable Date fechaDeCreacion){
        estadisticaService.cargarSnapshot(fechaDeCreacion);
        return ResponseEntity.ok("Snapshot cargada con éxito");
    }
}
