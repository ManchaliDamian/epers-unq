package ar.edu.unq.epersgeist.persistencia.dao.Exception;


import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

public class InsertarException extends RuntimeException {
    public InsertarException(Espiritu espiritu) {
        super("El espiritu [" + espiritu.getNombre() + "] no pudo ser insertado en la tabla");
    }
}