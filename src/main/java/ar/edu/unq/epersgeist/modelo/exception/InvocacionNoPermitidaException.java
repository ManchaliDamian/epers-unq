package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public class InvocacionNoPermitidaException extends IllegalArgumentException {
    public InvocacionNoPermitidaException(Espiritu espiritu, Ubicacion ubicacion) {
      super("El espiritu [" + espiritu.getNombre() + "] " +
              "no puede invocarse en [" + ubicacion.getNombre() + "]");
    }
}
