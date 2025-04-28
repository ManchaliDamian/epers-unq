package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MediumDAO extends JpaRepository<Medium, Long> {
    @Query("SELECT e FROM Espiritu e WHERE e.mediumConectado.id = :mediumId")
    List<Espiritu> findEspiritusByMediumId(@Param("mediumId") Long mediumId);



//    Medium crear(Medium unMedium);
//    Medium recuperar(Long mediumId);
//    List<Medium> recuperarTodos();
//    void actualizar(Medium unMedium);
//    void eliminar(Long mediumId);
//    List<Espiritu> espiritus(Long mediumId);
//    void eliminarTodo();
}