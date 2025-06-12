package ar.edu.unq.epersgeist.exception;

import jakarta.persistence.EntityNotFoundException;

public class UbicacionNoEncontradaException extends EntityNotFoundException {
    public UbicacionNoEncontradaException(Long id) {
        super(
                "Ubicacion no encontrada con ID: " + id
        );
    }

    public UbicacionNoEncontradaException(Double latitud, Double longitud) {
        super(
                "Ubicacion no encontrada que contenga la coordenada con latitud: " + latitud + " y longitud: " + longitud
        );
    }
}
