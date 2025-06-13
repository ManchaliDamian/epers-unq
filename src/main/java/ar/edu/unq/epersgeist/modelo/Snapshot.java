package ar.edu.unq.epersgeist.modelo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Snapshot {
    private String id;
    private Map<String, Object> sql;
    private Map<String, Object> mongo;
    private Map<String, Object> neo4j;
    private LocalDate fecha;

    public Snapshot(
            Map<String, Object> sql,
            Map<String, Object> neo4j,
            Map<String, Object> mongo,
            LocalDate fecha
    ) {
        this.sql = sql;
        this.neo4j = neo4j;
        this.mongo = mongo;
        this.fecha = fecha;
    }
}
