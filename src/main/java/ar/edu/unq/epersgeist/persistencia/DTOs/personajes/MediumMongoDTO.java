package ar.edu.unq.epersgeist.persistencia.DTOs.personajes;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import lombok.NoArgsConstructor;
import lombok.*;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.*;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor

@Document("Medium")
public class MediumMongoDTO {
    @Id
    private String idMediumMongo;

    private Long idMediumSQL;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint coordenada;

    public MediumMongoDTO(String idMediumMongo, Long idMediumSQL,  GeoJsonPoint coordenada){
        this.idMediumMongo = idMediumMongo;
        this.idMediumSQL = idMediumSQL;
        this.coordenada = coordenada;
    }


}
