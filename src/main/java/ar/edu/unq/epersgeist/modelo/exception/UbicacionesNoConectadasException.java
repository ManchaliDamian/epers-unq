package ar.edu.unq.epersgeist.modelo.exception;

public class UbicacionesNoConectadasException extends RuntimeException {
  public UbicacionesNoConectadasException(Long idOrigen, Long idDestino) {
    super(
            "El id origen: " + idOrigen + ", no está conectado a la id destino: " + idDestino
    );
  }
}
