package ar.edu.unq.epersgeist.exception;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;

public class EspirituNoDominableException extends RuntimeException {
    public EspirituNoDominableException(Long espirituADominar, Long espiritu) {
        super(" El espiritu con id: " + espirituADominar + "no puede dominar al Espiritu con id: " + espiritu);
    }
}
