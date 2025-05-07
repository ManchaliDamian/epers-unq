package ar.edu.unq.epersgeist.modelo.exception;

public class MediumEliminadoException extends RuntimeException {
    public MediumEliminadoException(Long idMedium) {
        super("El medium con id:" + idMedium + "ha sido eliminado");
    }
}
