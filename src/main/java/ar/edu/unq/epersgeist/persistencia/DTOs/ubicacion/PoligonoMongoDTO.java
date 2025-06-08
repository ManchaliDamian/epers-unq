package ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion;

import ar.edu.unq.epersgeist.modelo.exception.PoligonoIncompletoException;
import lombok.*;

import org.springframework.data.geo.Point;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@EqualsAndHashCode
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "poligonos")
public class PoligonoMongoDTO {
    @EqualsAndHashCode.Include
    @Id
    private String id;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPolygon poligono;

    private Long ubicacionId;

    public PoligonoMongoDTO(Long ubicacionId, List<Point> vertices) {
        if (vertices == null || vertices.size() < 4) {
            throw new PoligonoIncompletoException(
                    "La lista no puede ser nula y debe tener al menos 4 vértices."
            );
        }
        if (!vertices.getFirst().equals(vertices.getLast())) {
            throw new PoligonoIncompletoException("El primer vértice debe coincidir con el último.");
        }

        this.ubicacionId = ubicacionId;
        this.poligono = new GeoJsonPolygon(vertices);
    }
}
