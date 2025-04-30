package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.Cementerio;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.TipoUbicacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


public record UbicacionDTO(Long id, @NotBlank String nombre, @NotNull @Min(1) @Max(100) Integer energia, @NotNull TipoUbicacion tipo) {
    public static UbicacionDTO desdeModelo(Ubicacion ubicacion) {
        return new UbicacionDTO(
                ubicacion.getId(),
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
