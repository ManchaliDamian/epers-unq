package ar.edu.unq.epersgeist.modelo.exception;

public class EspirituEliminadoException extends RuntimeException {
    public EspirituEliminadoException(Long idEspiritu) {
        super("El espiritu con id:" + idEspiritu + "ha sido eliminado");
    }
}
