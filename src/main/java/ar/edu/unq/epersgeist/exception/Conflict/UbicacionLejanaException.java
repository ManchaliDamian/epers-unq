package ar.edu.unq.epersgeist.exception.Conflict;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public class UbicacionLejanaException extends ConflictException {
    public UbicacionLejanaException(Ubicacion origen, Ubicacion destino) {
        super("No existe conexión directa entre " + origen + " con ID " + origen.getId()
                + " y " + destino + " con ID " + destino.getId()
        );
    }

    public UbicacionLejanaException(Double distancia) {
        super("La ubicacion se encuentra a " + distancia + " km de distancia");
    }
}
