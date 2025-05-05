package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Ubicacion;

import java.util.Optional;

public interface RecuperarUbicacionService {
    Optional<Ubicacion> recuperar(Long ubicacionId);
}
