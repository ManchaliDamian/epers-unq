package ar.edu.unq.epersgeist.modelo.exception;

public class EspirituNoEliminableException extends RecursoNoEliminableException {
    public EspirituNoEliminableException(Long idEspiritu ) {
        super(
                "No es posible eliminar al Espiritu con ID: " +idEspiritu + " porque posee un medium conectado"
        );
    }
}
