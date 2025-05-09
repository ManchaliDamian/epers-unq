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
        Integer mana,
        Set<EspirituDTO> espiritus
) {
    public static CreateMediumDTO desdeModelo(Medium medium) {
        Set<EspirituDTO> espiritusDTO = medium.getEspiritus()
                .stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toSet());

        return new CreateMediumDTO(
                medium.getNombre(),
                medium.getUbicacion() != null ? medium.getUbicacion().getId() : null,
                medium.getManaMax(),
                medium.getMana(),
                espiritusDTO
        );
    }

    public Medium aModelo(Ubicacion ubicacion) {
        Medium medium = new Medium(nombre, manaMax, mana, ubicacion);

        if (espiritus != null) {
            for (EspirituDTO dto : espiritus) {
                Espiritu e = dto.aModelo();
                medium.conectarseAEspiritu(e);
            }
        }

        return medium;
    }
}
