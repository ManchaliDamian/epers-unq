package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.exception.BadRequest.PoligonoIncompletoException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@ToString
public class Poligono {

    private String id;
    private List<Coordenada> vertices;

    public Poligono(List<Coordenada> vertices) {
        if (vertices == null || vertices.size() < 4) {
            throw new PoligonoIncompletoException(
                    "La lista no puede ser nula y debe tener al menos 4 vértices."
            );
        }
        if (!vertices.get(0).equals(vertices.get(vertices.size() - 1))) {
            throw new PoligonoIncompletoException(
                    "El primer vértice debe coincidir con el último."
            );
        }
        for (int i = 0; i < vertices.size() - 1; i++) {
            if (vertices.get(i).equals(vertices.get(i + 1))) {
                throw new PoligonoIncompletoException(
                        "No puede haber vértices consecutivos idénticos."
                );
            }
        }
        this.vertices = List.copyOf(vertices);
    }
}
