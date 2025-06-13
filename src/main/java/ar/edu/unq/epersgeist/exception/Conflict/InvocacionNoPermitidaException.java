package ar.edu.unq.epersgeist.exception.Conflict;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

public class InvocacionNoPermitidaException extends ConflictException {
    public InvocacionNoPermitidaException(Espiritu espiritu, Ubicacion ubicacion) {
      super("El espiritu [" + espiritu.getNombre() + "] " +
              "no puede invocarse en [" + ubicacion.getNombre() + "]");
    }
}
