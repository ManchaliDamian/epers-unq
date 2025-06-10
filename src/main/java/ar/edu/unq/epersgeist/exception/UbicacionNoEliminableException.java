package ar.edu.unq.epersgeist.exception;

public class UbicacionNoEliminableException extends RecursoNoEliminableException {
    public UbicacionNoEliminableException(Long id) {
        super(
                "No es posible eliminar la Ubicacion con ID: " + id + " porque posee al menos un Espiritu o un Medium"
        );
    }
}
