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
    Boolean tieneConexiones(@Param("id") Long id);

    @Query(
            "MATCH (inicio:UbicacionNeoDTO {id: $idOrigen}), " +
                    "(fin:UbicacionNeoDTO {id: $idDestino})" +
                    "RETURN EXISTS((inicio)-[:CONECTA]->(fin))"
    )
    Boolean estanConectadas(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino );

    @Query(
            "MATCH (origen:UbicacionNeoDTO {id: $idOrigen}), (destino:UbicacionNeoDTO {id: $idDestino}) " +
                    "MERGE (origen)-[:CONECTA]->(destino)"
    )
    void conectar(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);

    @Query(
            "MATCH (inicio:UbicacionNeoDTO {id: $idOrigen}), " +
                    "(fin:UbicacionNeoDTO {id: $idDestino})" +
                    "MATCH caminos = shortestPath((inicio)-[:CONECTA*]->(fin))" +
                    "RETURN nodes(caminos) AS ubicaciones"
    )
    List<UbicacionNeoDTO> caminoMasCorto(@Param("idOrigen") Long idOrigen, @Param("idDestino") Long idDestino);


    @Query(
            "MATCH (u:UbicacionNeoDTO {id: $id})-[:CONECTA]->(v:UbicacionNeoDTO) " +
                    "RETURN v"
    )
    List<UbicacionNeoDTO> recuperarConexiones(@Param("id") Long id);

    @Query(
            "MATCH (nodo:UbicacionNeoDTO {id: $idUbicacion}) " +
            "WHERE id(nodo) = :idUbicacion" +
            "WITH size((nodo)--()) * 1.0 AS distancia" +
            "RETURN distancia"
    )
    Double degreeOf(@Param("idUbicacion") Long idUbicacion);

    @Query(
        "MATCH (u:UbicacionNeoDTO) DETACH DELETE u"
    )
    void deleteAll();
}
