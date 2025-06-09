package ar.edu.unq.epersgeist.modelo.exception;

import jakarta.persistence.EntityNotFoundException;

public class EspirituMuyLejanoException extends EntityNotFoundException {
    public EspirituMuyLejanoException(Long idEspiritu, Long idMedium) {
        super("El medium: " + idMedium + "está muy lejano del espiritu: " + idEspiritu);
    }
}
