package ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.List;


@Document
public class UbicacionMongoDTO {
    @EqualsAndHashCode.Include
    @Id
    private Long id;

    // private GeoJson<CoordenadaMongoDTO> poligono;
    private boolean deleted = false;

    public UbicacionMongoDTO(Long id) {
        this.id = id;

    }
}
