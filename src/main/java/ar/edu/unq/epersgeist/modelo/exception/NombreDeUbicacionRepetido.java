package ar.edu.unq.epersgeist.modelo.exception;

final public class NombreDeUbicacionRepetido extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NombreDeUbicacionRepetido(String message) {
        super("El nombre de ubicacion [" + message + "] ya esta siendo utilizado y no puede volver a crearse");
    }
}
