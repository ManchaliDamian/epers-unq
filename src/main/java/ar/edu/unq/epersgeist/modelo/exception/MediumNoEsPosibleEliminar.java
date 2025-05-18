package ar.edu.unq.epersgeist.modelo.exception;

public class MediumNoEsPosibleEliminar extends RuntimeException {
    public MediumNoEsPosibleEliminar(Long idMedium) {
        super(
                "No es posible eliminar el Medium con ID: " + idMedium + "porque posee espiritus"
        );
    }
}
