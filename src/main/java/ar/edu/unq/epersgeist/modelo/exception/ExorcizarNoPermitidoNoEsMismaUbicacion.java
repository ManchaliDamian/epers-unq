package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public class ExorcizarNoPermitidoNoEsMismaUbicacion extends RuntimeException {
    public ExorcizarNoPermitidoNoEsMismaUbicacion(Ubicacion ubicacion, Medium medium) {
        super("El Medium [" + medium.getNombre() + "] " +
                "no esta en la misma ubicacion [" + ubicacion.getNombre() + "que el otro medium]");
    }
}
