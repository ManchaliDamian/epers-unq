package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicacionDAONeo extends Neo4jRepository<UbicacionNeoDTO, Long> {


    @Query(
            "MATCH (u:UbicacionNeoDTO {id: $id})-[:CONECTA]-()" +
                    "RETURN COUNT(*) > 0")
    boolean tieneConexiones(@Param("id") Long id);

    @Query(
            "MATCH (a:UbicacionNeoDTO {id: $idOrigen}), (b:UbicacionNeoDTO {id: $idDestino})\n" +
                    "RETURN EXISTS((a)-[:CONECTA]->(b))"
    )
    boolean estanConectados(@Param("idOrigen") Long idOrigen,@Param("idDestino") Long idDestino );

    @Query(
            "MATCH (a:UbicacionNeoDTO {id: $idOrigen}), (b:UbicacionNeoDTO {id: $idDestino})" +
                    "MATCH p = shortestPath((a)-[:CONECTA*]->(b))" +
                    "RETURN nodes(p) AS ubicaciones"
    )
    List<UbicacionNeoDTO> caminoMasCortoEntre(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);

    @Query(
        "MATCH (u:UbicacionNeoDTO) DETACH DELETE u"
    )
    void deleteAll();
}
