package ar.edu.unq.epersgeist.controller.dto;

import java.time.LocalDateTime;

public record ConexionDTO(Long origenId, Long destinoId, LocalDateTime fechaConexion) {
    public static ConexionDTO desde(Long origenId, Long destinoId) {
        return new ConexionDTO(origenId, destinoId, LocalDateTime.now());
    }
}
