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
    public static CreateMediumDTO desdeModelo(Medium medium) {
        return new CreateMediumDTO(
                medium.getNombre(),
                medium.getUbicacion() != null ? medium.getUbicacion().getId() : null,
                medium.getManaMax(),
                medium.getMana()
        );
    }

    public Medium aModelo(Ubicacion ubicacion) {
        return new Medium(this.nombre(), this.manaMax(), this.mana(), ubicacion);
    }
}
