package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;

public class InvocacionNoPermitidaException extends RuntimeException {
    public InvocacionNoPermitidaException(Espiritu espiritu, Ubicacion ubicacion) {
      super("El espiritu [" + espiritu.getNombre() + "] " +
              "no puede invocarse en [" + ubicacion.getNombre() + "]");
    }
}
