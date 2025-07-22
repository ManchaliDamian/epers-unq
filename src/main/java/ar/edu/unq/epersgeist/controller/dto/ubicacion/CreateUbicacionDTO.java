package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateUbicacionDTO(
        @NotBlank String nombre,
        @NotNull @Min(1) @Max(100) Integer flujoDeEnergia,
        @NotNull TipoUbicacion tipo,
        @NotNull PoligonoDTO poligono
) {
    public Ubicacion aModelo() {
        return switch (this.tipo()) {
            case SANTUARIO -> new Santuario(this.nombre(), this.flujoDeEnergia());
            case CEMENTERIO -> new Cementerio(this.nombre(), this.flujoDeEnergia());
        };
    }
}
