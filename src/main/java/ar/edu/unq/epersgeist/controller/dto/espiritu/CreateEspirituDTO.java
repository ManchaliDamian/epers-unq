package ar.edu.unq.epersgeist.controller.dto.espiritu;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.CoordenadaDTO;
import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateEspirituDTO(
        @NotBlank String nombre,
        @NotNull  Long ubicacionId,
        @NotNull  TipoEspiritu tipo,
        @NotNull  CoordenadaDTO coordenada,
        int ataque,
        int defensa
) {

    public static CreateEspirituDTO desdeModelo(Espiritu espiritu, Coordenada coordenada) {
        return new CreateEspirituDTO(
                espiritu.getNombre(),
                espiritu.getUbicacion() != null ? espiritu.getUbicacion().getId() : null,
                espiritu.getTipo(),
                CoordenadaDTO.desdeModelo(coordenada),
                espiritu.getAtaque(),
                espiritu.getDefensa()
        );
    }

    public Espiritu aModeloEspiritu(Ubicacion ubicacion){
        return switch (this.tipo()) {
            case ANGELICAL -> new EspirituAngelical(nombre, ubicacion, ataque, defensa);
            case DEMONIACO  -> new EspirituDemoniaco(nombre, ubicacion, ataque, defensa);
        };
    }

    public Coordenada aModeloCoordenada(){
        return coordenada.aModelo();
    }
}
