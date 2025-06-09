package ar.edu.unq.epersgeist.controller.dto.medium;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.CoordenadaDTO;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public record CreateMediumDTO(
        @NotBlank String nombre,
        @NotNull Long ubicacionId,
        Integer manaMax,
        Integer mana,
        CoordenadaDTO coordenadaDTO
) {
    public static CreateMediumDTO desdeModelo(Medium medium) {
        return new CreateMediumDTO(
                medium.getNombre(),
                medium.getUbicacion() != null ? medium.getUbicacion().getId() : null,
                medium.getManaMax(),
                medium.getMana(),
                CoordenadaDTO.desdeModelo(medium.getCoordenada())
        );
    }

    public Medium aModelo(Ubicacion ubicacion) {
        Medium medium = new Medium(this.nombre(), this.manaMax(), this.mana(), ubicacion, coordenadaDTO.aModelo());
        medium.setCoordenada(coordenadaDTO.aModelo());

        return medium;
    }
}
