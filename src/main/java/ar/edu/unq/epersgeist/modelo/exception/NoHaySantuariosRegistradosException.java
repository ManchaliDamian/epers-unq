package ar.edu.unq.epersgeist.modelo.exception;

import jakarta.persistence.EntityNotFoundException;

public class NoHaySantuariosRegistradosException extends EntityNotFoundException {
  public NoHaySantuariosRegistradosException() {
    super("No hay santuarios registrados");
  }
}
