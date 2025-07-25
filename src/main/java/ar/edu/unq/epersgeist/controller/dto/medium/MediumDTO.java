package ar.edu.unq.epersgeist.controller.dto.medium;

import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;
import java.util.stream.Collectors;

public record MediumDTO(
        Long id,
        @NotBlank String nombre,
        UbicacionDTO ubicacion,
        Integer manaMax,
        Integer mana,
        Set<EspirituDTO> espiritus
) {
    public static MediumDTO desdeModelo(Medium medium) {
        Set<EspirituDTO> espiritusDTO = medium.getEspiritus()
                .stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toSet());

        return new MediumDTO(
                medium.getId(),
                medium.getNombre(),
                UbicacionDTO.desdeModelo(medium.getUbicacion()),
                medium.getManaMax(),
                medium.getMana(),
                espiritusDTO
        );
    }

    public Medium aModelo() {

        Medium medium = new Medium(nombre, manaMax, mana, ubicacion.aModelo());

        if (espiritus != null) {
            for (EspirituDTO dto : espiritus) {
                Espiritu e = dto.aModelo();
                medium.conectarseAEspiritu(e);
            }
        }

        return medium;
    }
}
