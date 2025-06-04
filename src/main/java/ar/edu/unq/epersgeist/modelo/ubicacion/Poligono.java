package ar.edu.unq.epersgeist.modelo.ubicacion;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Poligono {

    private String id;
    private List<Coordenada> vertices;

    public Poligono(List<Coordenada> coordenadas){
        this.vertices = coordenadas;
    }
}
