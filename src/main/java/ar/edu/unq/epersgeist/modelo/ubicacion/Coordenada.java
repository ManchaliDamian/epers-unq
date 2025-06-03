package ar.edu.unq.epersgeist.modelo.ubicacion;

import lombok.*;

@Getter @Setter
public class Coordenada {

    private Double longitud;
    private Double latitud;

    public Coordenada(Double latitud, Double longitud){
        this.latitud =latitud ;
        this.longitud = longitud;
    }

}
