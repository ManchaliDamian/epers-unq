package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Santuario;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
@Repository
public interface UbicacionDAO extends JpaRepository<Ubicacion, Long> {

    @Query("from Espiritu e where e.ubicacion.id = :ubicacionId")
    List<Espiritu> findEspiritusByUbicacionId(Long ubicacionId);

    @Query("from Medium m where m.ubicacion.id = :ubicacionId and size(m.espiritus) = 0")
    List<Medium> findMediumsSinEspiritusByUbicacionId(Long ubicacionId);

    //-----------------------------------------------------------------

    @Query(
            "SELECT u FROM Ubicacion u JOIN Espiritu e ON u.id = e.ubicacion.id " +
                    "WHERE TYPE(u) = Santuario " +
                    "GROUP BY u " +
                    "ORDER BY SUM(CASE WHEN TYPE(e) = EspirituDemoniaco THEN 1 ELSE 0 END) - " +
                    "SUM(CASE WHEN TYPE(e) = EspirituAngelical THEN 1 ELSE 0 END) DESC"
    )
    List<Santuario> obtenerSantuariosOrdenadosPorCorrupcion(Pageable pageable);

    @Query(
            "SELECT m FROM Medium m LEFT JOIN m.espiritus e " +
                    "WHERE m.ubicacion.id = :ubicacionId AND TYPE(e) = EspirituDemoniaco " +
                    "GROUP BY m " +
                    "ORDER BY COUNT(e) DESC, m.mana DESC"
    )
    List<Medium> mediumConMayorDemoniacosEn(@Param("ubicacionId") Long ubicacionId, Pageable pageable);

    @Query(
        "SELECT COUNT(e) FROM Espiritu e " +
        "WHERE TYPE(e) = EspirituDemoniaco AND e.ubicacion.id = :ubicacionId"
    )
    int cantTotalDeDemoniacosEn(@Param("ubicacionId") Long ubicacionId);

    @Query(
         "SELECT COUNT(e) FROM Espiritu e " +
         "WHERE TYPE(e) = EspirituDemoniaco AND e.ubicacion.id = :ubicacionId AND e.mediumConectado is NULL "
    )
    int cantTotalDeDemoniacosLibresEn(@Param("ubicacionId") Long ubicacionId);

}
