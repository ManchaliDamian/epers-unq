package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;

public class ConectarException extends RuntimeException {
    public ConectarException(Espiritu espiritu) {
        super("El espiritu [" + espiritu.getNombre() + "] ya esta conectado a un Medium");
    }
}
