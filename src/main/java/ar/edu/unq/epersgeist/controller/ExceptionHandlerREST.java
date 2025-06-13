package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.ErrorDTO;
import ar.edu.unq.epersgeist.exception.BadRequest.BadRequestException;
import ar.edu.unq.epersgeist.exception.Conflict.ConflictException;
import ar.edu.unq.epersgeist.exception.Conflict.DistanciaNoCercanaException;
import ar.edu.unq.epersgeist.exception.Conflict.EspirituNoDominableException;
import ar.edu.unq.epersgeist.exception.Conflict.NombreDeUbicacionRepetidoException;
import ar.edu.unq.epersgeist.exception.Conflict.RecursoNoEliminable.RecursoNoEliminableException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerREST {

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleConflict(ConflictException ex) {
        return new ErrorDTO(ex.getMessage(), HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(EntityNotFoundException ex) {
        return new ErrorDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            BadRequestException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(RuntimeException ex) {
        return new ErrorDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
}

