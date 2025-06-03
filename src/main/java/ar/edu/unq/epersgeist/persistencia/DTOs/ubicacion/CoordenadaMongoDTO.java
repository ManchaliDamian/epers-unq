package ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion;

import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "coordenadas")
public class CoordenadaMongoDTO {
        @Id
        private String id;

        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        private GeoJsonPoint punto;

        private Long entidadId;

        private TipoDeEntidad tipoDeEntidad;

        public CoordenadaMongoDTO(GeoJsonPoint punto, Long entidadId, TipoDeEntidad tipoDeEntidad) {
            this.punto = punto;
            this.entidadId = entidadId;
            this.tipoDeEntidad = tipoDeEntidad;
        }
}
