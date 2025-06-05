package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.controller.dto.medium.MediumDTO;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;

public record ReporteSantuarioCorruptoDTO(String nombreDelSantuario, MediumDTO mediumConMasDemonios, Integer cantTotalDemonios, Integer cantDemoniosLibres) {
    public static ReporteSantuarioCorruptoDTO desdeModelo(ReporteSantuarioMasCorrupto reporte) {
        return new ReporteSantuarioCorruptoDTO(
                reporte.getNombreSantuario(),
                reporte.getMediumMayorDemoniacos() != null ? MediumDTO.desdeModelo(reporte.getMediumMayorDemoniacos()) : null,
                reporte.getTotalDemonios(),
                reporte.getCantDemoniosLibres());
    }
}
