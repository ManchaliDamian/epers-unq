package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;

public class EspirituNoEstaEnLaMismaUbicacionException extends IllegalArgumentException {

  public EspirituNoEstaEnLaMismaUbicacionException(Espiritu espiritu, Medium medium) {
      super( "El espiritu {" + espiritu.getNombre()
              + "} no está en la misma ubicación que el medium {"
              + medium.getNombre() + "}");
  }
}
