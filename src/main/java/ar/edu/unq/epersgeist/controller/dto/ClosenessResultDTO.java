package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.servicios.interfaces.ClosenessResult;

import java.util.List;

public record ClosenessResultDTO(
        UbicacionDTO ubicacion,
        Double closeness
) {

    public static ClosenessResultDTO desdeModelo(ClosenessResult r) {
        return new ClosenessResultDTO(
                UbicacionDTO.desdeModelo(r.ubicacion()),
                r.closeness()
        );
    }

    public static List<ClosenessResultDTO> desdeModelo(List<ClosenessResult> lista) {
        return lista.stream()
                .map(ClosenessResultDTO::desdeModelo)
                .toList();
    }
}
