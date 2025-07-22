package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.Snapshot;
import ar.edu.unq.epersgeist.persistencia.DTOs.estadistica.SnapshotMongoDTO;
import org.springframework.stereotype.Component;

@Component("snapshotMapperImpl")
public class SnapshotMapperImpl implements SnapshotMapper{

    @Override
    public Snapshot toDomain(SnapshotMongoDTO snapshotMongoDTO) {
        Snapshot snapshot =
            new Snapshot(
                snapshotMongoDTO.getSql(),
                snapshotMongoDTO.getMongo(),
                snapshotMongoDTO.getNeo4j(),
                snapshotMongoDTO.getFecha());
        snapshot.setId(snapshotMongoDTO.getId());
        return snapshot;
    }

    @Override
    public SnapshotMongoDTO toMongo(Snapshot snapshot) {
        SnapshotMongoDTO dto =
                new SnapshotMongoDTO(
                        snapshot.getSql(),
                        snapshot.getMongo(),
                        snapshot.getNeo4j(),
                        snapshot.getFecha());
        if (snapshot.getId() != null) dto.setId(snapshot.getId());
        return dto;
    }
}
