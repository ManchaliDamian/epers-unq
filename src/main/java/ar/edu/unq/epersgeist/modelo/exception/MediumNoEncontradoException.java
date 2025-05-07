package ar.edu.unq.epersgeist.modelo.exception;

public class MediumNoEncontradoException extends RuntimeException{
    public MediumNoEncontradoException(Long mediumId){
        super( "Medium  con id: {" + mediumId
                + "} no encontrado ");
    }
}
