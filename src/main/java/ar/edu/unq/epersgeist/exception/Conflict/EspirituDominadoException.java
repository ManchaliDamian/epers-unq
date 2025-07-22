package ar.edu.unq.epersgeist.exception.Conflict;

public class EspirituDominadoException extends ConflictException {
  public EspirituDominadoException(String nombre) {
    super("El espíritu '" + nombre + "' está siendo dominado y no puede conectarse.");
  }
}
