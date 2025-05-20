package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.CementerioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.SantuarioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UbicacionDAOSQL extends JpaRepository<UbicacionJPADTO, Long>{

    @Query(
            "from Ubicacion u where u.deleted = false"
    )
    List<UbicacionJPADTO> recuperarTodos();

    @Query(
            "from Santuario s where s.deleted = false"
    )
    List<SantuarioJPADTO>  recuperarSantuarios();

    @Query(
            "from Cementerio c where c.deleted = false"
    )
    List<CementerioJPADTO>  recuperarCementerios();

    @Query(
            "from Ubicacion u where u.deleted = true"
    )
    List<UbicacionJPADTO> recuperarTodosEliminados();

    @Query(
            "from Ubicacion u where u.id = :id and u.deleted = true"
    )
    Optional<UbicacionJPADTO> recuperarEliminado(@Param("id") Long id);

    @Query("from Espiritu e where e.ubicacion.id = :ubicacionId " +
            "and e.deleted = false and e.ubicacion.deleted = false")
    List<Espiritu> findEspiritusByUbicacionId(Long ubicacionId);

    @Query("from Medium m where m.ubicacion.id = :ubicacionId and size(m.espiritus) = 0 " +
            "and m.deleted = false and m.ubicacion.deleted = false")
    List<Medium> findMediumsSinEspiritusByUbicacionId(Long ubicacionId);

    @Query("from Medium m where m.ubicacion.id = :ubicacionId " +
            "and m.deleted = false and m.ubicacion.deleted = false")
    List<Medium> findMediumByUbicacionId(Long ubicacionId);


    //-----------------------------------------------------------------

    @Query(
            "SELECT s FROM Santuario s JOIN Espiritu e ON s.id = e.ubicacion.id " +
                    "GROUP BY s " +
                    "ORDER BY SUM(CASE WHEN TYPE(e) = EspirituDemoniaco THEN 1 ELSE 0 END) - " +
                    "SUM(CASE WHEN TYPE(e) = EspirituAngelical THEN 1 ELSE 0 END) DESC"
    )
    List<SantuarioJPADTO> obtenerSantuariosOrdenadosPorCorrupcion(Pageable pageable);

    @Query(
            "SELECT m FROM Medium m  " +
                    "JOIN m.espiritus e ON m.id = e.mediumConectado.id AND e.deleted = false AND TYPE(e) = EspirituDemoniaco " +
                    "WHERE m.ubicacion.id = :ubicacionId " +
                    "AND m.deleted = false AND m.ubicacion.deleted = false " +
                    "GROUP BY m " +
                    "ORDER BY COUNT(e) DESC"
    )
    List<Medium> mediumConMayorDemoniacosEn(@Param("ubicacionId") Long ubicacionId);

    @Query("SELECT COUNT(e) FROM Espiritu e " +
            "WHERE TYPE(e) = EspirituDemoniaco " +
            "AND e.ubicacion.id = :ubicacionId " +
            "AND e.deleted = false")
    int cantTotalDeDemoniacosEn(@Param("ubicacionId") Long ubicacionId);

    @Query("SELECT COUNT(e) FROM Espiritu e " +
            "WHERE TYPE(e) = EspirituDemoniaco " +
            "AND e.ubicacion.id = :ubicacionId " +
            "AND e.mediumConectado IS NULL " +
            "AND e.deleted = false")
    int cantTotalDeDemoniacosLibresEn(@Param("ubicacionId") Long ubicacionId);

}
