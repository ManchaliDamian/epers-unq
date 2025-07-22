package ar.edu.unq.epersgeist.exception.NotFound;

import jakarta.persistence.EntityNotFoundException;

public class NoHaySantuarioCorruptoException extends EntityNotFoundException {
    public NoHaySantuarioCorruptoException() {
        super("No hay un santuario mas corrupto");
    }
}
