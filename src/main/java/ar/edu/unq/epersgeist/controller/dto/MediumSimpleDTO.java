package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Medium;

public record MediumSimpleDTO(Long id, String nombre) {
    public static MediumSimpleDTO desdeModelo(Medium medium) {
        return new MediumSimpleDTO(
                medium.getId(),
                medium.getNombre());
    }
}