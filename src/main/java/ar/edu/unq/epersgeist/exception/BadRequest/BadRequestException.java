package ar.edu.unq.epersgeist.exception.BadRequest;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String mensaje) {
        super(mensaje);
    }
}
