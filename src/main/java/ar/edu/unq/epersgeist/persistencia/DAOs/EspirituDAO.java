package ar.edu.unq.epersgeist.persistencia.DAOs;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.persistencia.EspirituJPA.EspirituAngelicalJPA;
import ar.edu.unq.epersgeist.persistencia.EspirituJPA.EspirituDemoniacoJPA;
import ar.edu.unq.epersgeist.persistencia.EspirituJPA.EspirituJPA;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspirituDAO extends JpaRepository<EspirituJPA, Long> {
    @Query(
            "FROM EspirituJPA e where e.deleted = false"
    )
    List<EspirituJPA> recuperarTodos();

    @Query("FROM EspirituDemoniacoJPA e where e.deleted = false")
    List<EspirituDemoniacoJPA> recuperarDemonios();

    @Query("FROM EspirituAngelicalJPA e where e.deleted = false")
    List<EspirituAngelicalJPA> recuperarAngeles();

    @Query(
            "FROM EspirituJPA e where e.deleted = true and e.id = :id"
    )
    Optional<EspirituJPA> recuperarEliminado(@Param("id") Long id);

    @Query(
            "FROM EspirituJPA e where e.deleted = true"
    )
    List<EspirituJPA> recuperarTodosLosEliminados();

    @Query(
            "FROM EspirituAngelicalJPA e where e.mediumConectado.id = :mediumId " +
                    "and e.deleted = false and e.mediumConectado.deleted = false"
    )
    List<EspirituAngelicalJPA> recuperarAngelesDe(@Param("mediumId") Long mediumId);

    @Query(
            "FROM EspirituDemoniacoJPA e where e.mediumConectado.id = :mediumId " +
                    "and e.deleted = false and e.mediumConectado.deleted = false"
    )
    List<EspirituDemoniacoJPA> recuperarDemoniosDe(@Param("mediumId") Long mediumId);


    @Query("FROM EspirituDemoniacoJPA e where e.deleted = false")
    List<EspirituJPA> recuperarDemoniacosPaginados(Pageable pageable);
}