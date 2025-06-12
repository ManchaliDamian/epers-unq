package ar.edu.unq.epersgeist.exception;

public class UbicacionYaTienePoligonoException extends RuntimeException {
    public UbicacionYaTienePoligonoException(String m ) {
        super("Poligono ya con id:" + m + "ya tiene asignado una ubicacion");
    }
}
