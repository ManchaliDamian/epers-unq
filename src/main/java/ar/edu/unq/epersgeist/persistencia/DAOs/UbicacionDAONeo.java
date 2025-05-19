package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.modelo.ubicacion.UbicacionNeo;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UbicacionDAONeo extends Neo4jRepository<UbicacionNeo, Long> {

    Optional<UbicacionNeo> findByNombre(@Param("nombre") String nombre);

    @Query("" +
            "MATCH (u:Ubicacion {id: $id})-[r]-(:Ubicacion)" +
            "RETURN COUNT(r) > 0 AS tieneConexiones")
    boolean tieneConexiones(@Param("id") Long id);
}
