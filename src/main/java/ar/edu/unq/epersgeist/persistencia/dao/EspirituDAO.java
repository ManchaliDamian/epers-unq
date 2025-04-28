package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
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
            "FROM EspirituAngelical e where e.mediumConectado.id = :mediumId"
    )
    List<EspirituAngelical> recuperarAngelesDe(@Param("mediumId") Long mediumId);

    @Query(
            "FROM EspirituDemoniaco e where e.mediumConectado.id = :mediumId"
    )
    List<EspirituDemoniaco> recuperarDemoniosDe(@Param("mediumId") Long mediumId);


    @Query("FROM Espiritu e where TYPE(e) = EspirituDemoniaco")
    List<Espiritu> recuperarDemoniacosPaginados(Pageable pageable);
}