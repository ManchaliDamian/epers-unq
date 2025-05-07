package ar.edu.unq.epersgeist.modelo.exception;

import jakarta.persistence.EntityNotFoundException;

public class MediumNoEncontradoException extends EntityNotFoundException {
    public MediumNoEncontradoException(Long mediumId){
        super( "Medium  con id: {" + mediumId
                + "} no encontrado ");
    }
}
