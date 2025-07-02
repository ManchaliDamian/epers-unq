package ar.edu.unq.epersgeist.exception.Conflict;

public class EspirituConectadoException extends ConflictException {
    public EspirituConectadoException(Long id) {
        super("El espíritu con id " + id + " está conectado a un medium");
    }
}
