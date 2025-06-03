package ar.edu.unq.epersgeist.modelo.exception;

public class PoligonoIncompletoException extends RuntimeException {
    public PoligonoIncompletoException() {
        super("El area debe ser cerrada para ser un poligono.");
    }
}
