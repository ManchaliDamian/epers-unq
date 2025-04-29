package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

//Ver luego que agregar, falta agregar UbicacionDTO 
//A lo último, terminar de agregar el tipo de espiritu
public record EspirituDTO(Long id, String nombre,Integer nivelDeConexion,Long mediumConectadoId) {
    public static EspirituDTO desdeModelo(Espiritu espiritu) {
        return new EspirituDTO(
                espiritu.getId(),
                espiritu.getNombre(),
                espiritu.getNivelDeConexion(),
                espiritu.getMediumConectado() != null ? espiritu.getMediumConectado().getId() : null
        );
    }

//Preguntar si se puede usar el switch case. Ya que, se necesita saber si es angelical o demoniaco
//Luego lo termino cuando estén hechos los de UbicaciónDTO, pero es algo así
// UbicacionDTO ubicacion en el record
//    public Espiritu aModelo(){
//        Espiritu espiritu = new Espiritu(this.nombre, ubicacion.aModelo());
//        espiritu.setId(this.id);
//        return espiritu;
//    }
}
