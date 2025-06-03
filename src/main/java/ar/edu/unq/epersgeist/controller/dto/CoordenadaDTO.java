package ar.edu.unq.epersgeist.controller.dto;

import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import org.springframework.lang.NonNull;

public record CoordenadaDTO(Double latitud, Double longitud) {

    public static CoordenadaDTO desdeModelo(Coordenada coordenada){
        return new CoordenadaDTO(coordenada.getLatitud(),coordenada.getLongitud());
    }

    public @NonNull Coordenada aModelo(){
        return new Coordenada(this.latitud,this.longitud);
    }

}
