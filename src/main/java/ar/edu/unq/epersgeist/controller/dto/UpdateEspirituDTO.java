package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import jakarta.validation.constraints.NotBlank;

public record UpdateEspirituDTO(@NotBlank String nombre) {

    public void actualizarModelo(Espiritu espiritu) {
        espiritu.setNombre(this.nombre());
    }
}
