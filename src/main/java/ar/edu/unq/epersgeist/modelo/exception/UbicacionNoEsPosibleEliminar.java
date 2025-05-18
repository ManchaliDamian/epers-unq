package ar.edu.unq.epersgeist.modelo.exception;

public class UbicacionNoEsPosibleEliminar extends RuntimeException {
    public UbicacionNoEsPosibleEliminar(Long id) {
        super(
                "No es posible eliminar la Ubicacion con ID:" + id + "porque posee al menos un Espiritu o un Medium"
        );
    }
}
