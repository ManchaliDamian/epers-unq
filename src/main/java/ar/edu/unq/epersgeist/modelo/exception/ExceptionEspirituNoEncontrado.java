package ar.edu.unq.epersgeist.modelo.exception;

public class ExceptionEspirituNoEncontrado extends RuntimeException {
    public ExceptionEspirituNoEncontrado(Long id) {
        super("No se encontró el espíritu con ID:" + id);
    }
}
