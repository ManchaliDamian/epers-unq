package ar.edu.unq.epersgeist.exception.NotFound;

import jakarta.persistence.EntityNotFoundException;

public class MediumNoEncontradoException extends EntityNotFoundException {
    public MediumNoEncontradoException(Long mediumId){
        super( "Medium  con id: {" + mediumId
                + "} no encontrado ");
    }
}
