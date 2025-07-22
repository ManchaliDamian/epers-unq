package ar.edu.unq.epersgeist.exception.Conflict.RecursoNoEliminable;

import ar.edu.unq.epersgeist.exception.Conflict.ConflictException;

public abstract class RecursoNoEliminableException extends ConflictException {
    public RecursoNoEliminableException(String mensaje) {
        super(mensaje);
    }
}
