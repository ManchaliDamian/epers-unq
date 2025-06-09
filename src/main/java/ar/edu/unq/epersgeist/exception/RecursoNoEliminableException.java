package ar.edu.unq.epersgeist.exception;

public abstract class RecursoNoEliminableException extends RuntimeException {
    public RecursoNoEliminableException(String mensaje) {
        super(mensaje);
    }
}
