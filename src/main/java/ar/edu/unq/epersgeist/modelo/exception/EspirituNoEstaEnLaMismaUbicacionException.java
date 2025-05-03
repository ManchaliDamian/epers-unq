package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

public class EspirituNoEstaEnLaMismaUbicacionException extends RuntimeException {

  public EspirituNoEstaEnLaMismaUbicacionException(Espiritu espiritu, Medium medium) {
      super( "El espiritu {" + espiritu.getNombre()
              + "} no está en la misma ubicación que el medium {"
              + medium.getNombre() + "}");
  }
}
