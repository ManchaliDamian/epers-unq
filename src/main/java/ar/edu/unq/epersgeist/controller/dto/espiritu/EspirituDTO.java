package ar.edu.unq.epersgeist.controller.dto.espiritu;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;

public record EspirituDTO(Long id, String nombre, Integer nivelDeConexion, Long mediumConectadoId,
                          UbicacionDTO ubicacion, TipoEspiritu tipo) {
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

    public Espiritu aModelo() {
        Espiritu e = switch (this.tipo) {
            case ANGELICAL  -> new EspirituAngelical(nombre, ubicacion.aModelo());
            case DEMONIACO   -> new EspirituDemoniaco(nombre, ubicacion.aModelo());
        };

        e.setId(id);
        e.setNivelDeConexion(nivelDeConexion);

        if (mediumConectadoId != null) {
            Medium m = new Medium();
            m.setId(mediumConectadoId);
            e.setMediumConectado(m);
        }

        return e;
    }

}