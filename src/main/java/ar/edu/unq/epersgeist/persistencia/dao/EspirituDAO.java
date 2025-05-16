package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspirituDAO extends JpaRepository<Espiritu, Long> {
    @Query(
            "FROM Espiritu e where e.deleted = false"
    )
    List<Espiritu> recuperarTodos();

    @Query("FROM EspirituDemoniaco e where e.deleted = false")
    List<EspirituDemoniaco> recuperarDemonios();

    @Query("FROM EspirituAngelical e where e.deleted = false")
    List<EspirituAngelical> recuperarAngeles();

    @Query(
            "FROM Espiritu e where e.deleted = true and e.id = :id"
    )
    Optional<Espiritu> recuperarEliminado(@Param("id") Long id);
    @Query(
            "FROM Espiritu e where e.deleted = true"
    )
    List<Espiritu> recuperarTodosLosEliminados();

    @Query(
            "FROM EspirituAngelical e where e.mediumConectado.id = :mediumId"
    )
    List<EspirituAngelical> recuperarAngelesDe(@Param("mediumId") Long mediumId);

    @Query(
            "FROM EspirituDemoniaco e where e.mediumConectado.id = :mediumId"
    )
    List<EspirituDemoniaco> recuperarDemoniosDe(@Param("mediumId") Long mediumId);


    @Query("FROM EspirituDemoniaco e where e.deleted = false")
    List<Espiritu> recuperarDemoniacosPaginados(Pageable pageable);
}