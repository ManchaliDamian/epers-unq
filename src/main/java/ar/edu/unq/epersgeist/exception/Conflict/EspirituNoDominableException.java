package ar.edu.unq.epersgeist.exception.Conflict;

public class EspirituNoDominableException extends ConflictException {
    public EspirituNoDominableException(Long espirituADominar, Long espiritu) {
        super(" El espiritu con id: " + espirituADominar + "no puede dominar al Espiritu con id: " + espiritu);
    }
}
