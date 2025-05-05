package ar.edu.unq.epersgeist.modelo.exception;

public class UbicacionNoEncontrada extends RuntimeException{
    public UbicacionNoEncontrada(Long ubicacionId){
        super( "Ubicacion  con id: {" + ubicacionId
                + "} no encontrado ");
    }
}
