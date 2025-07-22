package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.exception.BadRequest.CoordenadaFueraDeRangoException;
import lombok.*;

@Getter @Setter @ToString
@EqualsAndHashCode
public class Coordenada {

    private Double longitud;
    private Double latitud;

    public Coordenada(Double latitud, Double longitud) {
        validarRango(latitud, longitud);
        this.latitud  = latitud;
        this.longitud = longitud;
    }

    private void validarRango(Double latitud, Double longitud) {
        if (latitud == null || longitud == null) {
            throw new CoordenadaFueraDeRangoException("Latitud o longitud nula");
        }
        if (latitud < -90.0 || latitud > 90.0 ||
                longitud < -180.0 || longitud > 180.0) {
            throw new CoordenadaFueraDeRangoException(
                    "Coordenada inválida: latitud debe estar entre -90 y +90, "
                            + "longitud entre -180 y +180"
            );
        }
    }

}
