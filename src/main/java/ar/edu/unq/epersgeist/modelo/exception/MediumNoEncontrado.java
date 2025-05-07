package ar.edu.unq.epersgeist.modelo.exception;

import jakarta.persistence.EntityNotFoundException;

public class MediumNoEncontrado extends EntityNotFoundException {
    public MediumNoEncontrado(Long mediumId){
        super( "Medium  con id: {" + mediumId
                + "} no encontrado ");
    }
}
