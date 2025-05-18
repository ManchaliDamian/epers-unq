package ar.edu.unq.epersgeist.modelo.exception;

public class EspirituNoEsPosibleEliminar extends RuntimeException {
    public EspirituNoEsPosibleEliminar(Long idEspiritu ) {
        super(
                "No es posible eliminar al Espiritu con ID: " +idEspiritu + "porque posee un medium conectado"
        );
    }
}
