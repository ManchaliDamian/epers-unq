package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UbicacionDAO extends JpaRepository<Ubicacion, Long> {

    @Query(
            "from Ubicacion u where u.deleted = false"
    )
    List<Ubicacion> recuperarTodos();

    @Query(
            "from Santuario s where s.deleted = false"
    )
    List<Santuario>  recuperarSantuarios();

    @Query(
            "from Cementerio c where c.deleted = false"
    )
    List<Cementerio>  recuperarCementerios();

    @Query(
            "from Ubicacion u where u.deleted = true"
    )
    List<Ubicacion> recuperarTodosEliminados();

    @Query(
            "from Ubicacion u where u.id = :id"
    )
    Optional<Ubicacion> recuperarEliminado(@Param("id") Long id);

    @Query("from Espiritu e where e.ubicacion.id = :ubicacionId and e.deleted = false")
    List<Espiritu> findEspiritusByUbicacionId(Long ubicacionId);

    @Query("from Medium m where m.ubicacion.id = :ubicacionId and size(m.espiritus) = 0 and m.deleted = false")
    List<Medium> findMediumsSinEspiritusByUbicacionId(Long ubicacionId);

    //-----------------------------------------------------------------

    @Query(
            "SELECT s FROM Santuario s JOIN Espiritu e ON s.id = e.ubicacion.id " +
                    "GROUP BY s " +
                    "ORDER BY SUM(CASE WHEN TYPE(e) = EspirituDemoniaco THEN 1 ELSE 0 END) - " +
                    "SUM(CASE WHEN TYPE(e) = EspirituAngelical THEN 1 ELSE 0 END) DESC"
    )
    List<Santuario> obtenerSantuariosOrdenadosPorCorrupcion(Pageable pageable);

    @Query(
            "SELECT m FROM Medium m LEFT JOIN m.espiritus e " +
                    "WHERE m.ubicacion.id = :ubicacionId AND TYPE(e) = EspirituDemoniaco " +
                    "GROUP BY m " +
                    "ORDER BY COUNT(e) DESC"
    )
    List<Medium> mediumConMayorDemoniacosEn(@Param("ubicacionId") Long ubicacionId, Pageable pageable);

    @Query(
        "SELECT COUNT(e) FROM EspirituDemoniaco e " +
                "WHERE e.ubicacion.id = :ubicacionId and e.deleted = false"
    )
    int cantTotalDeDemoniacosEn(@Param("ubicacionId") Long ubicacionId);

    @Query(
         "SELECT COUNT(e) FROM EspirituDemoniaco e " +
         "WHERE e.ubicacion.id = :ubicacionId AND e.mediumConectado is NULL and e.deleted = false"
    )
    int cantTotalDeDemoniacosLibresEn(@Param("ubicacionId") Long ubicacionId);

}
