package ar.edu.unq.epersgeist.controller.dto.estadistica;

import ar.edu.unq.epersgeist.modelo.Snapshot;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Map;

public record SnapshotDTO(
        Map<String, Object> sql,
        Map<String, Object> mongo,
        Map<String, Object> neo4j,
        @JsonFormat(pattern = "yyyy/MM/dd") LocalDate fecha
) {
    public static SnapshotDTO desdeModelo(Snapshot snapshot){
        return new SnapshotDTO(
                snapshot.getSql(),
                snapshot.getMongo(),
                snapshot.getNeo4j(),
                snapshot.getFecha()
        );
    }
}
