package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.ReporteSantuarioCorruptoDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estadistica")
public class EstadisticaREST {
    private final EstadisticaService estadisticaService;

    public EstadisticaREST(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping()
    public ReporteSantuarioCorruptoDTO getSantuarioMasCorrupto() {
        return ReporteSantuarioCorruptoDTO.desdeModelo(estadisticaService.santuarioCorrupto());
    }
}
