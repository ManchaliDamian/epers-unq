package ar.edu.unq.epersgeist.exception;

public class SuperposicionDePoligonosException extends RuntimeException {
    public SuperposicionDePoligonosException(String message) {
        super("El poligono con id: " + message + "se superposiciona con otro poligono");
    }
}
