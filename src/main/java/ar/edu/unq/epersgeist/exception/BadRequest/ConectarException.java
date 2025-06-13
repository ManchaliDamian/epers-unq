package ar.edu.unq.epersgeist.exception.BadRequest;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;

public class ConectarException extends IllegalArgumentException {
    public ConectarException(Espiritu espiritu, Medium medium) {
        super("El espiritu [" + espiritu.getNombre() + "] " +
                "no esta conectado al Medium [" + medium.getNombre() + "]");
    }
}
