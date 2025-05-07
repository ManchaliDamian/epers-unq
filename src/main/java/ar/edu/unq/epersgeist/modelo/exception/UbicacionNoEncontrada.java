package ar.edu.unq.epersgeist.modelo.exception;

import jakarta.persistence.EntityNotFoundException;

public class UbicacionNoEncontrada extends EntityNotFoundException {
        public UbicacionNoEncontrada(Long ubicacionId){
            super( "Ubicacion  con id: {" + ubicacionId
                    + "} no encontrada ");
        }
    }

