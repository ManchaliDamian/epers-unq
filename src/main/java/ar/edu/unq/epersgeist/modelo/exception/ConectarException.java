package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

public class ConectarException extends RuntimeException {
    public ConectarException(Espiritu espiritu, Medium medium) {
        super("El espiritu [" + espiritu.getNombre() + "] " +
                "no esta conectado al Medium [" + medium.getNombre() + "]");
    }
}
