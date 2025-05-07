package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;

public class ExceptionEspirituOcupado extends IllegalArgumentException {
  private final Espiritu espiritu;
    public ExceptionEspirituOcupado(Espiritu espiritu) {
        this.espiritu = espiritu;
    }

    @Override
    public String getMessage(){
      return "El Espiritu [" + espiritu.getNombre() + "] esta ocupado.";
    }
}
