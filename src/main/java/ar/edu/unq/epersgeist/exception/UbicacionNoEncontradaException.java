package ar.edu.unq.epersgeist.exception;

import jakarta.persistence.EntityNotFoundException;

public class UbicacionNoEncontradaException extends EntityNotFoundException {
    public UbicacionNoEncontradaException(Long id) {
        super(
                "Ubicacion no encontrada con ID: " + id
        );
    }
}
