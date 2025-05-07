package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.modelo.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @ExceptionHandler(MediumNoEncontrado.class)
    public ResponseEntity<Map<String, String>>  handleMediumNoEncontradoPorId(MediumNoEncontrado ex) {
        Map<String, String> error = new HashMap<>();
        error.put("response_code", "400");
        error.put("description", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvocacionNoPermitidaException.class)
    public ResponseEntity<Map<String, String>>  handleInvocacionNoPermitida(InvocacionNoPermitidaException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("response_code", "400");
        error.put("description", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ExorcizarNoPermitidoNoEsMismaUbicacion.class)
    public ResponseEntity<Map<String, String>>  handleExorcizarNoPermitidoNoEsMismaUbicacion(ExorcizarNoPermitidoNoEsMismaUbicacion ex) {
        Map<String, String> error = new HashMap<>();
        error.put("response_code", "400");
        error.put("description", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ExorcistaSinAngelesException.class)
    public ResponseEntity<Map<String, String>>  handleExorcistaSinAngelesException(ExorcistaSinAngelesException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("response_code", "400");
        error.put("description", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ExceptionEspirituOcupado.class)
    public ResponseEntity<Map<String, String>>  handleExceptionEspirituOcupado(ExceptionEspirituOcupado ex) {
        Map<String, String> error = new HashMap<>();
        error.put("response_code", "400");
        error.put("description", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(EspirituNoEstaEnLaMismaUbicacionException.class)
    public ResponseEntity<Map<String, String>>  handleEspirituNoEstaEnLaMismaUbicacionException(EspirituNoEstaEnLaMismaUbicacionException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("response_code", "400");
        error.put("description", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConectarException.class)
    public ResponseEntity<Map<String, String>>  handleConectarException(ConectarException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("response_code", "400");
        error.put("description", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

}
