package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituDAO extends JpaRepository<Espiritu, Long> {

    //Esto es momentaneamente, luego se quita ---------------------
    @Query(
            "FROM Espiritu e where e.id = :espirituId"
    )
    Espiritu recuperar(Long espirituId);
    //-------------------------------------------------------------

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