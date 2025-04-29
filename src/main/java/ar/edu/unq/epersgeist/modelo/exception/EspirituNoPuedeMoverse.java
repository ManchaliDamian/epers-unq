package ar.edu.unq.epersgeist.modelo.exception;

import ar.edu.unq.epersgeist.modelo.Espiritu;

public class EspirituNoPuedeMoverse extends RuntimeException {
    public EspirituNoPuedeMoverse(Espiritu e) {
        super("El espiritu: " + e.getNombre() + " no puede moverse por si solo.");
    }
}
