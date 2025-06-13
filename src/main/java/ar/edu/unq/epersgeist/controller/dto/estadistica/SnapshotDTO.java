package ar.edu.unq.epersgeist.controller.dto.estadistica;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter @Setter
public class SnapshotDTO {
    @Id
    private String id;
    private Date fecha;

    private Map<String, Object> sql;
    private Map<String, Object> mongo;
    private Map<String, Object> neo4j;
}
