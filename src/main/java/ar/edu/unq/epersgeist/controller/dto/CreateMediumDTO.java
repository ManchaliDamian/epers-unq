package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
