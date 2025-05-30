package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.servicios.interfaces.DegreeResult;

import java.util.List;

public record DegreeResultDTO(
        UbicacionDTO ubicacion,
        Double degreeCentrality
) {
    public static DegreeResultDTO desdeModelo(DegreeResult r) {
        return new DegreeResultDTO(
                UbicacionDTO.desdeModelo(r.ubicacion()),
                r.degree()
        );
    }

    public static List<DegreeResultDTO> desdeModelo(List<DegreeResult> lista) {
        return lista.stream()
                .map(DegreeResultDTO::desdeModelo)
                .toList();
    }
}