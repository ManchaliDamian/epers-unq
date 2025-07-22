package ar.edu.unq.epersgeist.exception.Conflict;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;

public class ExorcistaSinAngelesException extends ConflictException {
    public ExorcistaSinAngelesException(Medium medium) {
        super("El medium [" + medium.getNombre() + "] " +
                "no esta conectado al Medium a ningun angel");
    }
}
