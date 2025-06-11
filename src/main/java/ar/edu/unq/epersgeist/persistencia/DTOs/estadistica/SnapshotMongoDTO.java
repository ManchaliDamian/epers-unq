package ar.edu.unq.epersgeist.persistencia.DTOs.estadistica;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter @Setter
public class SnapshotMongoDTO {
    @Id
    private String id;

    private Map<String, Object> sql;
    private Map<String, Object> mongo;
    private Map<String, Object> neo4j;

    private Date fecha;

}
