package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;

public class EspirituOcupadoException extends IllegalArgumentException {
  private final Espiritu espiritu;
    public EspirituOcupadoException(Espiritu espiritu) {
        this.espiritu = espiritu;
    }

    @Override
    public String getMessage(){
      return "El Espiritu [" + espiritu.getNombre() + "] esta ocupado.";
    }
}
