package ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion;

import ar.edu.unq.epersgeist.modelo.exception.PoligonoIncompletoException;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.geo.Point;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "areas")
public class AreaMongoDTO {
    @Id
    private String id;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPolygon poligono;

    private Long ubicacionId;

    public AreaMongoDTO(List<Point> vertices) {
        if (vertices == null || vertices.size() < 4
                || !vertices.get(0).equals(vertices.get(vertices.size() - 1))) {
            throw new PoligonoIncompletoException();
        }
        this.poligono = new GeoJsonPolygon(vertices);
    }
}
