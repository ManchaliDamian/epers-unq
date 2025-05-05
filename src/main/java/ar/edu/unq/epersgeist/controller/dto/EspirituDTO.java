package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.TipoEspiritu;

public record EspirituDTO(Long id, String nombre,Integer nivelDeConexion, Long mediumConectadoId, UbicacionDTO ubicacion, TipoEspiritu tipo) {
    public static EspirituDTO desdeModelo(Espiritu espiritu) {
        return new EspirituDTO(
                espiritu.getId(),
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
                EspirituAngelical angelical = new EspirituAngelical(nombre,ubicacion.aModelo());
                angelical.setId(id);
                return angelical;
            }
            case DEMONIACO -> {
                EspirituDemoniaco demoniaco = new EspirituDemoniaco(nombre,ubicacion.aModelo());
                demoniaco.setId(id);
                return demoniaco;
            }
             default -> throw new IllegalArgumentException("Argumentos no validos");
        }
    }
}
