package ar.edu.unq.epersgeist.controller.dto;


import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import com.mongodb.lang.NonNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PoligonoDTO(@NotNull List<@Valid CoordenadaDTO> coordenadas) {

    public static PoligonoDTO desdeModelo(Poligono poligono) {
        List<CoordenadaDTO> coordenadasDTO = poligono.getVertices().stream()
                .map(CoordenadaDTO::desdeModelo)
                .toList();
        return new PoligonoDTO(coordenadasDTO);
    }

    public @NonNull Poligono aModelo() {
        List<Coordenada> coordenadasModelo = this.coordenadas.stream()
                .map(CoordenadaDTO::aModelo)
                .toList();
        return new Poligono(coordenadasModelo);
    }
}
