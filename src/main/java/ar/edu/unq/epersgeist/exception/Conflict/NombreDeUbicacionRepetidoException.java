package ar.edu.unq.epersgeist.exception.Conflict;

final public class NombreDeUbicacionRepetidoException extends ConflictException {

    public NombreDeUbicacionRepetidoException(String message) {
        super("El nombre de ubicacion [" + message + "] ya esta siendo utilizado y no puede volver a crearse");
    }
}
