package ar.edu.unq.epersgeist.exception.Conflict.RecursoNoEliminable;

public class MediumNoEliminableException extends RecursoNoEliminableException {
    public MediumNoEliminableException(Long idMedium) {
        super(
                "No es posible eliminar el Medium con ID: " + idMedium + " porque posee al menos un espiritu"
        );
    }
}
