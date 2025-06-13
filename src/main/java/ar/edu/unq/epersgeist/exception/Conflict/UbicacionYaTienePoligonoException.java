package ar.edu.unq.epersgeist.exception.Conflict;

public class UbicacionYaTienePoligonoException extends ConflictException {
    public UbicacionYaTienePoligonoException(String m ) {
        super("Poligono ya con id:" + m + "ya tiene asignado una ubicacion");
    }
}
