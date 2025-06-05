package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUbicacionDTO(@NotBlank String nombre,
                                 @NotNull @Min(1) @Max(100) Integer flujoDeEnergia) {

    public void actualizarModelo(Ubicacion ubicacion) {
        ubicacion.setNombre(this.nombre());
        ubicacion.setFlujoDeEnergia(this.flujoDeEnergia());
    }
}