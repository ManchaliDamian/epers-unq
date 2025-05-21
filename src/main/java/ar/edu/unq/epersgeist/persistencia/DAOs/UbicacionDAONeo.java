package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UbicacionDAONeo extends Neo4jRepository<UbicacionNeoDTO, Long> {


    @Query(
            "MATCH (u:Ubicacion {id: $id})-[r]-(:Ubicacion)" +
            "RETURN COUNT(r) > 0 AS tieneConexiones")
    boolean tieneConexiones(@Param("id") Long id);

    @Query(
            "MATCH (inicio:UbicacionNeo {id: $idOrigen}), (fin: UbicacionNeo {id: $idDestino}) " +
                    "RETURN EXISTS((inicio)-[:CondicionDeMutacion]->(fin))"
    )
    boolean estanConectados(@Param("idOrigen") Long idOrigen,@Param("idDestino") Long idDestino );

    @Query(
            "MATCH (inicio:Ubicacion {id: idOrigen}), (fin: Ubicacion {id: $idDestino}) " +
                    "MATCH caminos = shortestPath((inicio)-[:CondicionDeMutacion*]->(fin))" +
                    "return nodes(caminos)"
    )
    List<Ubicacion> caminoMasCortoEntre(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);

    @Query(
        "MATCH (u: Ubicacion) DETACH DELETE u"
    )
    void deleteAll();
}
