package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Cementerio;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateUbicacionDTO(@NotBlank String nombre, @NotNull @Min(1) @Max(100) Integer energia, @NotNull TipoUbicacion tipo) {
    public static CreateUbicacionDTO desdeModelo(Ubicacion ubicacion) {
        return new CreateUbicacionDTO(
                ubicacion.getNombre(),
                ubicacion.getFlujoDeEnergia(),
                ubicacion.getTipo()
        );
    }

    public Ubicacion aModelo() {
        return switch (this.tipo()) {
            case SANTUARIO -> new Santuario(this.nombre(), this.energia());
            case CEMENTERIO -> new Cementerio(this.nombre(), this.energia());
        };
    }
}
