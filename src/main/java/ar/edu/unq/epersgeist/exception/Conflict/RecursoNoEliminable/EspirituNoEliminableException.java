package ar.edu.unq.epersgeist.exception.Conflict.RecursoNoEliminable;

public class EspirituNoEliminableException extends RecursoNoEliminableException {
    public EspirituNoEliminableException(Long idEspiritu ) {
        super(
                "No es posible eliminar al Espiritu con ID: " +idEspiritu + " porque posee un medium conectado"
        );
    }
}
