package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;

public class EspirituNoEstaEnLaMismaUbicacionException extends RuntimeException {

  private final Espiritu espiritu;
  private final Medium medium;

  public EspirituNoEstaEnLaMismaUbicacionException(Espiritu espiritu, Medium medium) {
    this.espiritu = espiritu;
    this.medium = medium;
  }

    @Override
   public String getMessage(){
      return "El espiritu {" + espiritu.getUbicacion() + "} no está en la misma ubicación que el medium {" + medium.getNombre() + "}";
    }

}
