package ar.edu.unq.epersgeist.exception.Conflict;

public class UbicacionesNoConectadasException extends ConflictException {
  public UbicacionesNoConectadasException(Long idOrigen, Long idDestino) {
    super(
            "El id origen: " + idOrigen + ", no está conectado a la id destino: " + idDestino
    );
  }
}
