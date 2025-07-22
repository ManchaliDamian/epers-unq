package ar.edu.unq.epersgeist.exception.Conflict;

public class EspirituMuyLejanoException extends ConflictException {
    public EspirituMuyLejanoException(Long idEspiritu, Long idMedium) {
        super("El medium: " + idMedium + "está muy lejano del espiritu: " + idEspiritu);
    }
}
