package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.ErrorDTO;
import ar.edu.unq.epersgeist.exception.EspirituNoDominableException;
import ar.edu.unq.epersgeist.exception.NombreDeUbicacionRepetidoException;
import ar.edu.unq.epersgeist.exception.RecursoNoEliminableException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerREST {

    @ExceptionHandler({
            NombreDeUbicacionRepetidoException.class,
            RecursoNoEliminableException.class,
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleConflict(RuntimeException ex) {
        return new ErrorDTO(ex.getMessage(), HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(EntityNotFoundException ex) {
        return new ErrorDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
    @ExceptionHandler(EspirituNoDominableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleEspirituNoDominableException(EspirituNoDominableException ex) {
        return new ErrorDTO(ex.getMessage(), HttpStatus.CONFLICT.value());
    }
}

