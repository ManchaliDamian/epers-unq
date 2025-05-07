package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.modelo.exception.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerREST {

    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("response_code", String.valueOf(status.value()));
        error.put("description", message);
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NombreDeUbicacionRepetido.class)
    public ResponseEntity<Map<String, String>> handleConflict(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({
            EspirituNoEncontradoException.class,
            MediumNoEncontradoException.class,
            EntityNotFoundException.class
    })
    public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EspirituEliminadoException.class)
    public ResponseEntity<Map<String, String>> handleGone(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.GONE, ex.getMessage());
    }

    @ExceptionHandler({
            InvocacionNoPermitidaException.class,
            ExorcizarNoPermitidoNoEsMismaUbicacion.class,
            ExorcistaSinAngelesException.class,
            EspirituOcupadoException.class,
            EspirituNoEstaEnLaMismaUbicacionException.class,
            ConectarException.class
    })
    public ResponseEntity<Map<String, String>> handleBadRequest(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
