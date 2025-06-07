package ar.edu.unq.epersgeist.persistencia.DTOs.personajes;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "mediums")
public class MediumMongoDTO {
    @Id
    private String idMongo;

    private Long idSQL;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint punto;

    public MediumMongoDTO(GeoJsonPoint punto) {
        this.punto = punto;
    }
}
