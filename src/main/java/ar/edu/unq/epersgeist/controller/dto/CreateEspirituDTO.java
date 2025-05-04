package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.TipoEspiritu;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static ar.edu.unq.epersgeist.modelo.TipoEspiritu.DEMONIACO;
import static ar.edu.unq.epersgeist.modelo.TipoEspiritu.ANGELICAL;

//Ver luego que agregar, falta agregar UbicacionDTO
//A lo último, terminar de agregar el tipo de espiritu
public record CreateEspirituDTO(@NotBlank String nombre, @NotNull @Min(1) @Max(100) Integer nivelDeConexion, Long mediumConectadoId, UbicacionDTO ubicacion,@NotNull TipoEspiritu tipo) {
    public static CreateEspirituDTO desdeModelo(Espiritu espiritu) {
        return new CreateEspirituDTO(
                espiritu.getNombre(),
                espiritu.getNivelDeConexion(),
                espiritu.getMediumConectado() != null ? espiritu.getMediumConectado().getId() : null,
                UbicacionDTO.desdeModelo(espiritu.getUbicacion()),
                espiritu.getTipo()
        );
    }

    public Espiritu aModelo(){
        switch (this.tipo()){
            case ANGELICAL -> {
                return new EspirituAngelical(nombre,ubicacion.aModelo());
            }
            case DEMONIACO -> {
                return  new EspirituDemoniaco(nombre,ubicacion.aModelo());
            }
            default -> throw new IllegalArgumentException("Argumentos no validos");
        }
    }
}
