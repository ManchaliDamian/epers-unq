package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.ClosenessResultNeoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.DegreeResultNeoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UbicacionDAONeo extends Neo4jRepository<UbicacionNeoDTO, Long> {

    @Query("MATCH (u:UbicacionNeoDTO) WHERE u.idSQL = $idSQL RETURN u")
    Optional<UbicacionNeoDTO> findByIdSQL(@Param("idSQL") Long idSQL);

    @Query(
            "MATCH (u:UbicacionNeoDTO {idSQL: $idSQL})-[:CONECTA]-()" +
                    "RETURN COUNT(*) > 0")
    Boolean tieneConexiones(@Param("idSQL") Long idSQL);

    @Query(
            "MATCH (inicio:UbicacionNeoDTO {idSQL: $idOrigen}), " +
                    "(fin:UbicacionNeoDTO {idSQL: $idDestino})" +
                    "RETURN EXISTS((inicio)-[:CONECTA]->(fin))"
    )
    Boolean estanConectadas(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino );

    @Query(
            "MATCH (origen:UbicacionNeoDTO {idSQL: $idOrigen}), (destino:UbicacionNeoDTO {idSQL: $idDestino}) " +
                    "MERGE (origen)-[:CONECTA]->(destino)"
    )
    void conectar(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);

    @Query(
            "MATCH (inicio:UbicacionNeoDTO {idSQL: $idOrigen}), " +
                    "(fin:UbicacionNeoDTO {idSQL: $idDestino})" +
                    "MATCH caminos = shortestPath((inicio)-[:CONECTA*]->(fin))" +
                    "RETURN nodes(caminos) AS ubicaciones"
    )
    List<UbicacionNeoDTO> caminoMasCorto(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);


    @Query(
            "MATCH (u:UbicacionNeoDTO {idSQL: $idSQL})-[:CONECTA]->(v:UbicacionNeoDTO) " +
                    "RETURN v"
    )
    List<UbicacionNeoDTO> recuperarConexiones(@Param("idSQL") Long idSQL);

    @Query("""
        MATCH (n:UbicacionNeoDTO)
        WHERE n.idSQL IN $idsSQL
        OPTIONAL MATCH (n)-[r]-()
        RETURN n AS ubicacion, count(r) AS degree
    """)
    List<DegreeResultNeoDTO> degreeOf(@Param("idsSQL") List<Long> idsSQL);

    @Query("""
        MATCH (n:UbicacionNeoDTO), (other:UbicacionNeoDTO)
        WHERE n.idSQL IN $idsSQL AND other <> n
        OPTIONAL MATCH path = shortestPath((n)-[:CONECTA*]->(other))
        WITH n,
            sum(
                CASE
                    WHEN path IS NULL THEN 10
                    ELSE length(path)
                END
            ) AS totalDist
        RETURN n AS ubicacion, 1.0 / totalDist AS closeness
    """)
    List<ClosenessResultNeoDTO> closenessOf(@Param("idsSQL") List<Long> idsSQL);


    @Query(
        "MATCH (u:UbicacionNeoDTO) DETACH DELETE u"
    )
    void deleteAll();

}
