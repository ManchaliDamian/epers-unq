package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituAngelicalJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituDemoniacoJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspirituDAO extends JpaRepository<EspirituJPADTO, Long> {
    @Query(
            "FROM Espiritu e where e.deleted = false"
    )
    List<EspirituJPADTO> recuperarTodos();

    @Query("FROM EspirituDemoniaco e where e.deleted = false")
    List<EspirituDemoniacoJPADTO> recuperarDemonios();

    @Query("FROM EspirituAngelical e where e.deleted = false")
    List<EspirituAngelicalJPADTO> recuperarAngeles();

    @Query(
            "FROM Espiritu e where e.deleted = true and e.id = :id"
    )
    Optional<EspirituJPADTO> recuperarEliminado(@Param("id") Long id);

    @Query(
            "FROM Espiritu e where e.deleted = true"
    )
    List<EspirituJPADTO> recuperarTodosLosEliminados();

    @Query(
            "FROM EspirituAngelical e where e.mediumConectado.id = :mediumId " +
                    "and e.deleted = false and e.mediumConectado.deleted = false"
    )
    List<EspirituAngelicalJPADTO> recuperarAngelesDe(@Param("mediumId") Long mediumId);

    @Query(
            "FROM EspirituDemoniaco e where e.mediumConectado.id = :mediumId " +
                    "and e.deleted = false and e.mediumConectado.deleted = false"
    )
    List<EspirituDemoniacoJPADTO> recuperarDemoniosDe(@Param("mediumId") Long mediumId);


    @Query("FROM EspirituDemoniaco e where e.deleted = false")
    List<EspirituJPADTO> recuperarDemoniacosPaginados(Pageable pageable);
}