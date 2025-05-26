package ar.edu.unq.epersgeist.modelo.exception;

public class MismaUbicacionException extends IllegalArgumentException {
    public MismaUbicacionException() {
        super("No se puede conectar una ubicacion con sigo misma");
    }
}