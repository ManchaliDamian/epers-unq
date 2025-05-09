package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

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
