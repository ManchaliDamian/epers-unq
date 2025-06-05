package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CreateEspirituDTO(@NotBlank String nombre, @NotNull Long ubicacionId, @NotNull TipoEspiritu tipo) {
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