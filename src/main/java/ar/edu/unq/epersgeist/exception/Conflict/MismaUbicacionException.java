package ar.edu.unq.epersgeist.exception.Conflict;

public class MismaUbicacionException extends ConflictException {
    public MismaUbicacionException() {
        super("No se puede conectar una ubicacion con sigo misma");
    }
}