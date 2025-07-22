package ar.edu.unq.epersgeist.exception.Conflict;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public class ExorcizarNoPermitidoNoEsMismaUbicacion extends ConflictException {
    public ExorcizarNoPermitidoNoEsMismaUbicacion(Ubicacion ubicacion, Medium medium) {
        super("El Medium [" + medium.getNombre() + "] " +
                "no esta en la misma ubicacion [" + ubicacion.getNombre() + "que el otro medium]");
    }
}
