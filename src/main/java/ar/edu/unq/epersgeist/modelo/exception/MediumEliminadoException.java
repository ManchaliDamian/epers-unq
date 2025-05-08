package ar.edu.unq.epersgeist.modelo.exception;

public class MediumEliminadoException extends Throwable {
    public MediumEliminadoException(Long mediumId) {
        super("Medium Eliminado con ID:" + mediumId);
    }
}
