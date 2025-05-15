package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.ReporteSantuarioCorruptoDTO;
import ar.edu.unq.epersgeist.modelo.ubicaciones.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
