package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.ReporteSantuarioCorruptoDTO;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.persistencia.DTOs.estadistica.SnapshotMongoDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Date> crearSnapshot(){
        Date fechaDeCreacion = estadisticaService.guardarSnapshot();
        return ResponseEntity.ok(fechaDeCreacion);
    }

    @PostMapping("/snapshot/load/{date}")
    public ResponseEntity<SnapshotMongoDTO> crearSnapshot(){

    }
}
