package ar.edu.unq.epersgeist.exception.NotFound;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;

public class SnapshotNoEncontradoException extends EntityNotFoundException {
  public SnapshotNoEncontradoException(LocalDate date) {
    super(
            "No existe un snapshot correspondiente a la fecha " + date
    );
  }
}
