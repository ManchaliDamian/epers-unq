package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.estadistica.ReporteSantuarioCorruptoDTO;
import ar.edu.unq.epersgeist.controller.dto.estadistica.SnapshotDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/estadistica")
public final class EstadisticaControllerREST {

    private final EstadisticaService estadisticaService;

    public EstadisticaControllerREST(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping("/santuarioMasCorrupto")
    public ResponseEntity<ReporteSantuarioCorruptoDTO> obtenerSantuarioMasCorrupto() {
        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();

        ReporteSantuarioCorruptoDTO dto = ReporteSantuarioCorruptoDTO.desdeModelo(reporte);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/snapshot")
    public ResponseEntity<String> crearSnapshot(){
        estadisticaService.crearSnapshot();
        return ResponseEntity.ok("Snapshot creada con éxito");
    }

    @GetMapping("/snapshot/{fechaDeCreacion}")
    public ResponseEntity<SnapshotDTO> obtenerSnapshot(@PathVariable @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate fechaDeCreacion){
        SnapshotDTO snapshot = SnapshotDTO.desdeModelo(estadisticaService.obtenerSnapshot(fechaDeCreacion));
        return ResponseEntity.ok(snapshot);
    }
}
