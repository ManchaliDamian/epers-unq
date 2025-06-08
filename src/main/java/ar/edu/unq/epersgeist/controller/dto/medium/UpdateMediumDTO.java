package ar.edu.unq.epersgeist.controller.dto.medium;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import jakarta.validation.constraints.NotBlank;

public record UpdateMediumDTO(
        @NotBlank String nombre,
        Integer manaMax
) {
    public void actualizarModelo(Medium medium) {
        medium.setNombre(this.nombre());
        medium.setManaMax(this.manaMax());
    }
}
