package ar.edu.unq.epersgeist.controller.dto.medium;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.CoordenadaDTO;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMediumDTO(
        @NotBlank String nombre,
        @NotNull Long ubicacionId,
        @NotNull @Min(0) Integer manaMax,
        @NotNull @Min(0) Integer mana,
        @NotNull CoordenadaDTO coordenadaDTO
) {
    public static CreateMediumDTO desdeModelo(Medium medium, Coordenada coordenada) {
        return new CreateMediumDTO(
                medium.getNombre(),
                medium.getUbicacion() != null ? medium.getUbicacion().getId() : null,
                medium.getManaMax(),
                medium.getMana(),
                CoordenadaDTO.desdeModelo(coordenada)
        );
    }

    public Medium aModeloMedium(Ubicacion ubicacion) {

        return new Medium(this.nombre(), this.manaMax(), this.mana(), ubicacion);
    }

    public Coordenada aModeloCoordenada() {
        return coordenadaDTO.aModelo();
    }
}
