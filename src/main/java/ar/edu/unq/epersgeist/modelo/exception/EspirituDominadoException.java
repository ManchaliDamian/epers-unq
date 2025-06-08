package ar.edu.unq.epersgeist.modelo.exception;

public class EspirituDominadoException extends RuntimeException {
  public EspirituDominadoException(String nombre) {
    super("El espíritu '" + nombre + "' está siendo dominado y no puede conectarse.");
  }
}
