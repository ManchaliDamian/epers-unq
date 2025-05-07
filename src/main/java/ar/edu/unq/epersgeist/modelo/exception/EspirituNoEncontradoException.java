package ar.edu.unq.epersgeist.modelo.exception;

public class EspirituNoEncontradoException extends RuntimeException {
    public EspirituNoEncontradoException(Long id) {
        super("No se encontró el espíritu con ID:" + id);
    }
}
