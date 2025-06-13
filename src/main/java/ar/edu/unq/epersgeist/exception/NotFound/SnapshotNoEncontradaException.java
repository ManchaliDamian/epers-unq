package ar.edu.unq.epersgeist.exception.NotFound;

import jakarta.persistence.EntityNotFoundException;

import java.util.Date;

public class SnapshotNoEncontradaException extends EntityNotFoundException {
  public SnapshotNoEncontradaException(Date date) {
    super(
            "No existe un snapshot correspondiente a la fecha " + date
    );
  }
}
