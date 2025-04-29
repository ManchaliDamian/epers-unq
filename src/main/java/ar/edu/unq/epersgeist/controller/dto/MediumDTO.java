package ar.edu.unq.epersgeist.controller.dto;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record MediumDTO(
        Long id,
        @NotBlank String nombre,
        Ubicacion ubicacion,
        Integer manaMax,
        Integer mana,
        List<String> espiritus
        ) {

    public static MediumDTO desdeModelo(Medium medium){

    };

    public static MediumDTO aModelo(Medium medium){

    };
}
