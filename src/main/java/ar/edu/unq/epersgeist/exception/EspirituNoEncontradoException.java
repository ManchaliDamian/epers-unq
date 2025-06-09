package ar.edu.unq.epersgeist.exception;

import jakarta.persistence.EntityNotFoundException;

public class EspirituNoEncontradoException extends EntityNotFoundException {
    public EspirituNoEncontradoException(Long id) {
        super("No se encontró el espíritu con ID: " + id);
    }
}
