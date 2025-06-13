package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.Snapshot;
import ar.edu.unq.epersgeist.persistencia.DTOs.estadistica.SnapshotMongoDTO;

public interface SnapshotMapper {
    Snapshot toDomain(SnapshotMongoDTO snapshotMongoDTO);
    SnapshotMongoDTO toMongo(Snapshot snapshot);
}
