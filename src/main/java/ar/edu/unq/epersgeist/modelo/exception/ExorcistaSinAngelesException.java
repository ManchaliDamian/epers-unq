package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Medium;

public class ExorcistaSinAngelesException extends RuntimeException{
    public ExorcistaSinAngelesException(Medium medium) {
        super("El medium [" + medium.getNombre() + "] " +
                "no esta conectado al Medium a ningun angel");
    }
}
