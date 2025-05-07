package ar.edu.unq.epersgeist.modelo.exception;

import jakarta.persistence.EntityNotFoundException;

public class EspirituNoEncontrado extends EntityNotFoundException {
    public EspirituNoEncontrado(Long espirituId){
        super( "Espiritu  con id: {" + espirituId
                + "} no encontrado ");
    }
}
