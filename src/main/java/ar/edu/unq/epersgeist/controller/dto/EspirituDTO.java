package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.*;

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

    public Espiritu aModelo() {
        Espiritu e = switch (this.tipo) {
            case ANGELICAL  -> new EspirituAngelical(nombre, ubicacion.aModelo());
            case DEMONIACO   -> new EspirituDemoniaco(nombre, ubicacion.aModelo());
            default         -> throw new IllegalArgumentException("Tipo no válido: " + tipo);
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