package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public record ClosenessResult(Ubicacion ubicacion, Double closeness) { }