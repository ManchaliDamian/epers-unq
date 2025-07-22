package ar.edu.unq.epersgeist.exception.Conflict;

public class EspirituMuertoException extends ConflictException {
    public EspirituMuertoException(Long id) {
        super("El espíritu con id " + id + " está muerto");
    }
}
