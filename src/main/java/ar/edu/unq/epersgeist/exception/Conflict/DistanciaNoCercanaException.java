package ar.edu.unq.epersgeist.exception.Conflict;

public class DistanciaNoCercanaException extends ConflictException {
    public DistanciaNoCercanaException(String message) {
        super(message);
    }

    public static class EspirituOcupadoException extends ConflictException {
        public EspirituOcupadoException(String nombre) {
            super("El Espiritu ["+ nombre +"] esta ocupado.");
        }
    }
}
