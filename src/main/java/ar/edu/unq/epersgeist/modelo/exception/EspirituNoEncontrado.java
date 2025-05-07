package ar.edu.unq.epersgeist.modelo.exception;

public class EspirituNoEncontrado extends RuntimeException{
    public EspirituNoEncontrado(Long espirituId){
        super( "Espiritu  con id: {" + espirituId
                + "} no encontrado ");
    }
}
