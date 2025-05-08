package ar.edu.unq.epersgeist.modelo.exception;

public class MediumEliminadoException extends RuntimeException {
    public MediumEliminadoException(Long mediumId) {
        super("Medium eliminado con ID:" + mediumId);
    }
}
