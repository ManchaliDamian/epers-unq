package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;

public class ExceptionEspirituEliminado extends RuntimeException {
    public ExceptionEspirituEliminado(Long idEspiritu) {
        super("El espiritu con id:" + idEspiritu + "ha sido eliminado");
    }
}
