package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;

public record ClosenessResult(UbicacionDTO ubicacion, Double closeness) { }