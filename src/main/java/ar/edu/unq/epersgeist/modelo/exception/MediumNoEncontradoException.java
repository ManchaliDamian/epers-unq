package ar.edu.unq.epersgeist.modelo.exception;

public class MediumNoEncontrado extends RuntimeException{
    public MediumNoEncontrado(Long mediumId){
        super( "Medium  con id: {" + mediumId
                + "} no encontrado ");
    }
}
