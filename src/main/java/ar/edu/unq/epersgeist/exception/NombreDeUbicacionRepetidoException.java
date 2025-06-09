package ar.edu.unq.epersgeist.exception;

final public class NombreDeUbicacionRepetidoException extends RuntimeException {

    public NombreDeUbicacionRepetidoException(String message) {
        super("El nombre de ubicacion [" + message + "] ya esta siendo utilizado y no puede volver a crearse");
    }
}
