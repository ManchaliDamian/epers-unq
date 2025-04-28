package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.TipoUbicacion;
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
        return switch (tipo) {
            case SANTUARIO -> new Santuario(nombre, energia);
            case CEMENTERIO -> new Cementerio(nombre, energia);
        };
    }
}
