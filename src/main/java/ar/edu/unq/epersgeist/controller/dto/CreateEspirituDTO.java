package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateEspirituDTO(@NotBlank String nombre, Long mediumConectadoId, @NotNull Long ubicacionId, @NotNull TipoEspiritu tipo) {
    public static CreateEspirituDTO desdeModelo(Espiritu espiritu) {
        return new CreateEspirituDTO(
                espiritu.getNombre(),
                espiritu.getMediumConectado() != null ? espiritu.getMediumConectado().getId() : null,
                espiritu.getUbicacion() != null ? espiritu.getUbicacion().getId() : null,
                espiritu.getTipo()
        );
    }

    public Espiritu aModelo(Ubicacion ubicacion){
        switch (this.tipo()){
            case ANGELICAL -> {
                return new EspirituAngelical(nombre, ubicacion);
            }
            case DEMONIACO -> {
                return new EspirituDemoniaco(nombre, ubicacion);
            }
            default -> throw new IllegalArgumentException("Argumentos no validos");
        }
    }
}
