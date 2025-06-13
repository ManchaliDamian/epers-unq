package ar.edu.unq.epersgeist.exception.Conflict;

public class SuperposicionDePoligonosException extends ConflictException {
    public SuperposicionDePoligonosException(String message) {
        super("El poligono con id: " + message + "se superposiciona con otro poligono");
    }
}
