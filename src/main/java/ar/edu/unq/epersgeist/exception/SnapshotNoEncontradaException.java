package ar.edu.unq.epersgeist.exception;

import java.util.Date;

public class SnapshotNoEncontradaException extends RuntimeException {
  public SnapshotNoEncontradaException(Date date) {
    super(
            "No existe un snapshot correspondiente a la fecha " + date
    );
  }
}
