package ar.edu.unq.epersgeist.exception.Conflict;

public class NombreDeEspirituRepetidoException extends ConflictException {
    public NombreDeEspirituRepetidoException(String message) {
        super("El nombre de espiritu [" + message + "] ya esta siendo utilizado y no puede volver a crearse");

    }
}
