package ar.edu.unq.epersgeist.modelo.exception;

public class EspirituMuyLejanoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EspirituMuyLejanoException() {
    }

    @Override
    public String getMessage() {
        return "El espiritu se encuentra demasiado lejos";
    }
}
