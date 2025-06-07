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

    public MediumMongoDTO(String idMediumMongo, Long idMediumSQL){
        this.idMediumMongo = idMediumMongo;
        this.idMediumSQL = idMediumSQL;
    }

    public static MediumMongoDTO desdeModelo(Medium medium){
        MediumMongoDTO dto = new MediumMongoDTO();
        dto.idMediumSQL = medium.getId();
        dto.coordenada = convertirAGeoJson(medium.getCoordenada());
        dto.idMediumMongo = medium.getMongoId();
        return dto;
    }

    public Medium aModelo(){
        Medium medium = new Medium();
        medium.setMongoId(this.idMediumMongo);
        medium.setId(this.idMediumSQL);
        medium.setCoordenada(convertirACoordenada(this.coordenada));
        return medium;
    }

    private static Coordenada convertirACoordenada(GeoJsonPoint coordenada){
        return new Coordenada(coordenada.getX(),coordenada.getY());
    }

    private static GeoJsonPoint convertirAGeoJson(Coordenada coordenada){
        return new GeoJsonPoint(coordenada.getLongitud(),coordenada.getLatitud());
    }

}
