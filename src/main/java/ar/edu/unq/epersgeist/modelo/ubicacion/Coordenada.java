package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import lombok.*;

@Getter @Setter
public class Coordenada {

    private String id;
    private Double longitud;
    private Double latitud;

    public Coordenada(Double latitud, Double longitud){
        this.latitud =latitud ;
        this.longitud = longitud;
    }

    public Double calcularDistanciaA(Coordenada coordenadaMedium, Espiritu espiritu){
        Double latM = coordenadaMedium.getLatitud();
        Double longM = coordenadaMedium.getLongitud();

        Double latE = coordenadaMedium.getLatitud();
        Double longE = coordenadaMedium.getLongitud();
        return 0.0;
        //return this.calularDistanciaHaversine(latM,longM,latE,longE);
    }

}
