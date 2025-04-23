package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspirituDAO extends JpaRepository<Espiritu, Long> {

    //VER COMO ARREGLARLO
    @Query(
            "FROM Espiritu e where e.id = :espirituId"
    )
    Espiritu recuperar(Long espirituId);
    //------------------------
    @Query(
            "FROM EspirituAngelical e where e.mediumConectado.id = :mediumId"
    )
    List<EspirituAngelical> recuperarAngelesDe(Long mediumId);

    @Query(
            "FROM EspirituDemoniaco e where e.mediumConectado.id = :mediumId"
    )
    List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId);

    //Ver luego como hacer el de paginados

//Borrar luego  -------------------------------------------------------
//    void guardar(Espiritu espiritu);
//    Espiritu recuperar(Long idDelEspiritu);
//    List<Espiritu> recuperarTodos();
//    void actualizar(Espiritu espiritu);
//    void eliminar(Espiritu espiritu);
//    void eliminarTodo();
//    List<Espiritu> recuperarDemoniacosPaginados(Direccion direccion, int pagina, int cantidadPorPagina);
//    List<EspirituAngelical> recuperarAngelesDe(Long mediumId);
//    List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId);
//-----------------------------------------------------------------------

}