package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.validation.constraints.NotNull;

public record UbicacionSimpleDTO(@NotNull Long id,
                           @NotNull TipoUbicacion tipo) {
    public static UbicacionSimpleDTO desdeModelo(Ubicacion ubicacion) {
        return new UbicacionSimpleDTO(
                ubicacion.getId(),
                ubicacion.getTipo()
        );
    }
}