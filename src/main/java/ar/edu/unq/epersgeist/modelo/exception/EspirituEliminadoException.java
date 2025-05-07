package ar.edu.unq.epersgeist.modelo.exception;

public class ExceptionEspirituEliminado extends RuntimeException {
    public ExceptionEspirituEliminado(Long idEspiritu) {
        super("El espiritu con id:" + idEspiritu + "ha sido eliminado");
    }
}
