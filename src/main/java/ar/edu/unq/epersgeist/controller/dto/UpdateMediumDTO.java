package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import jakarta.validation.constraints.NotBlank;

public record UpdateMediumDTO(
        @NotBlank String nombre,
        Integer manaMax
) {
    public static UpdateMediumDTO desdeModelo(Medium medium) {
        return new UpdateMediumDTO(
                medium.getNombre(),
                medium.getManaMax()
        );
    }
    public void actualizarModelo(Medium medium) {
        medium.setNombre(this.nombre());
        medium.setManaMax(this.manaMax());
    }
}
