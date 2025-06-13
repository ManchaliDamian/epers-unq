package ar.edu.unq.epersgeist.exception.NotFound;

import java.util.Date;

public class SnapshotNoEncontradaException extends RuntimeException {
  public SnapshotNoEncontradaException(Date date) {
    super(
            "No existe un snapshot correspondiente a la fecha " + date
    );
  }
}
