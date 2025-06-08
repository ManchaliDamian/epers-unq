package ar.edu.unq.epersgeist.exception;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public class UbicacionLejanaException extends RuntimeException {
    public UbicacionLejanaException(Ubicacion origen, Ubicacion destino) {
        super("No existe conexión directa entre " + origen + " y " + destino);
    }
}
