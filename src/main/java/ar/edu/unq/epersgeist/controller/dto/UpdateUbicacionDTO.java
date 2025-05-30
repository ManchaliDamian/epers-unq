package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.ubicaciones.Ubicacion;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUbicacionDTO(@NotBlank String nombre, @NotNull @Min(1) @Max(100) Integer flujoDeEnergia) {
    public static UpdateUbicacionDTO desdeModelo(Ubicacion ubicacion) {
        return new UpdateUbicacionDTO(
                ubicacion.getNombre(),
                ubicacion.getFlujoDeEnergia()
        );
    }

    public void actualizarModelo(Ubicacion ubicacion) {
        ubicacion.setNombre(this.nombre());
        ubicacion.setFlujoDeEnergia(this.flujoDeEnergia());
    }
}