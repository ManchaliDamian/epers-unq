package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.stream.Collectors;

public record MediumDTO(
        Long id,
        @NotBlank String nombre,
        Ubicacion ubicacion,
        Integer manaMax,
        Integer mana,
        List<EspirituDTO> espiritus
) {
    public static MediumDTO desdeModelo(Medium medium) {
        List<EspirituDTO> espiritusDTO = medium.getEspiritus()
                .stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toList());

        return new MediumDTO(
                medium.getId(),
                medium.getNombre(),
                medium.getUbicacion(),
                medium.getManaMax(),
                medium.getMana(),
                espiritusDTO
        );
    }

    public Medium aModelo() {
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
