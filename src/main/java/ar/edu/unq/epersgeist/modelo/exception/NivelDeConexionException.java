package ar.edu.unq.epersgeist.modelo.exception;

public class NivelDeConexionException extends RuntimeException {
    public NivelDeConexionException() {
        super("El nivel de conexión debe estar entre 0 y 100");
    }
}
