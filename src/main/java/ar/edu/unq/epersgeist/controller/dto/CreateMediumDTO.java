package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMediumDTO(
        @NotBlank String nombre,
        @NotNull Long ubicacionId,
        Integer manaMax,
        Integer mana
) {

    public Medium aModelo(Ubicacion ubicacion) {
        return new Medium(this.nombre(), this.manaMax(), this.mana(), ubicacion);
    }
}
