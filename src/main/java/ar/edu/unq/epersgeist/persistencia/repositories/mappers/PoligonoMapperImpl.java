package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("poligonoMapperImpl")
public class PoligonoMapperImpl implements PoligonoMapper {

    @Override
    public PoligonoMongoDTO toMongo(Long ubicacionId, Poligono poligono) {
        List<Point> vertices = poligono.getVertices().stream()
                .map(coordenada -> new Point(coordenada.getLongitud(), coordenada.getLatitud()))
                .collect(Collectors.toList());
        
        return new PoligonoMongoDTO(ubicacionId, vertices);
    }

    @Override
    public Poligono toDomain(PoligonoMongoDTO poligonoMongoDTO) {
        List<Coordenada> coordenadas = poligonoMongoDTO.getPoligono().getPoints().stream()
                .map(point -> new Coordenada(point.getY(), point.getX())) // Y=latitud, X=longitud
                .collect(Collectors.toList());
        
        return new Poligono(coordenadas);
    }
}