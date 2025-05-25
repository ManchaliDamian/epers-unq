package ar.edu.unq.epersgeist.modelo.exception;

public class UbicacionesNoConectadasException extends RuntimeException {
  public UbicacionesNoConectadasException(Long idOrigen, Long idDestino) {
    super(
            "El id origen: " + idOrigen + "No está conectado a la id Destino: " + idDestino
    );
  }
}
