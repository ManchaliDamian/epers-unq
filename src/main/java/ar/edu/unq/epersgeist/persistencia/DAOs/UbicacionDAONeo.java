package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionDAONeo extends Neo4jRepository<UbicacionNeoDTO, Long> {


    @Query(
            "MATCH (u:Ubicacion {id: $id})-[r]-(:Ubicacion)" +
            "RETURN COUNT(r) > 0 AS tieneConexiones")
    boolean tieneConexiones(@Param("id") Long id);
}
