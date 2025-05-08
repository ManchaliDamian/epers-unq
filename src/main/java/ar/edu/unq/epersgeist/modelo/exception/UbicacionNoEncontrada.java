package ar.edu.unq.epersgeist.modelo.exception;

public class UbicacionNoEncontrada extends RuntimeException {
    public UbicacionNoEncontrada(Long id) {
        super(
                "Ubicacion no encontrada con ID: " + id
        );
    }
}
