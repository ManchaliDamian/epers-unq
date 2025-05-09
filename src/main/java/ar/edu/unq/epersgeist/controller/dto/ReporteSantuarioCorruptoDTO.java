package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;

public record ReporteSantuarioCorruptoDTO(String nombreDelSantuario, MediumSimpleDTO mediumConMasDemonios, Integer cantTotalDemonios, Integer cantDemoniosLibres) {
    public static ReporteSantuarioCorruptoDTO desdeModelo(ReporteSantuarioMasCorrupto reporte) {
        return new ReporteSantuarioCorruptoDTO(
                reporte.getNombreSantuario(),
                reporte.getMediumMayorDemoniacos() != null ? MediumSimpleDTO.desdeModelo(reporte.getMediumMayorDemoniacos()) : null,
                reporte.getTotalDemonios(),
                reporte.getCantDemoniosLibres());
    }
}
