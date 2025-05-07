package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.ErrorDTO;
import ar.edu.unq.epersgeist.modelo.exception.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerREST {

    @ExceptionHandler(NombreDeUbicacionRepetido.class)
    public ResponseEntity<Map<String, String>>  handleNombreUbicacionRepetido(NombreDeUbicacionRepetido ex) {
        Map<String, String> error = new HashMap<>();
        error.put("response_code", "409");
        error.put("description", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorDTO> handleNotFoundException(EntityNotFoundException ex) {
        ErrorDTO errorDto = new ErrorDTO(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorDTO errorDto = new ErrorDTO(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(errorDto);
    }

}
