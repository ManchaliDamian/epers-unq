package ar.edu.unq.epersgeist.controller.dto;


import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;

public record EstadisticaDTO(String nombreSantuario, int totalDemonios,
                             int demoniosLibres, MediumDTO mediumConMasDemonios) {

    public static EstadisticaDTO desdeModelo(ReporteSantuarioMasCorrupto reporte) {
        return new EstadisticaDTO(
                reporte.getNombreSantuario(),
                reporte.getTotalDemonios(),
                reporte.getDemoniosLibres(),
                reporte.getMediumMayorDemoniacos() != null
                        ? MediumDTO.desdeModelo(reporte.getMediumMayorDemoniacos())
                        : null
        );
    }
}
