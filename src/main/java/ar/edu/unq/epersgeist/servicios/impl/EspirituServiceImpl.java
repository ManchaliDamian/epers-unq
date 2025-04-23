package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;

    public EspirituServiceImpl(EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public void guardar(Espiritu espiritu) {
            espirituDAO.save(espiritu);
    }

    //Esto tiene que ser Optional --------------------------------
    @Override
    public Espiritu recuperar(Long espirituId) {
        //Otra opción-----------------------------
        //return espirituDAO.findById(espirituId)
              //  .orElseThrow(() -> new NoSuchElementException("EspirituId not found: " + espirituId));
        //----------------------------------------
        return espirituDAO.recuperar(espirituId);
    }
    //----------------------------------------------------------------------------

    @Override
    public List<Espiritu> recuperarTodos() {
        return espirituDAO.findAll();
    }

    @Override
    public void actualizar(Espiritu espiritu) {
//        HibernateTransactionRunner.runTrx(() -> {
//            espirituDAO.actualizar(espiritu);
//            return null;
//        });
    }

    @Override
    public void eliminar(Long espirituId) {
//        HibernateTransactionRunner.runTrx(() -> {
//            Espiritu espiritu = espirituDAO.recuperar(espirituId);
//            espirituDAO.eliminar(espiritu);
//            return null;
//        });
    }

    @Override
    public void eliminarTodo(){
        espirituDAO.deleteAll();
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
//        return HibernateTransactionRunner.runTrx(() -> {
//            Espiritu espiritu = espirituDAO.recuperar(espirituId);
//            Medium medium = mediumDAO.recuperar(mediumId);
//
//            medium.conectarseAEspiritu(espiritu);
//
//            espirituDAO.actualizar(espiritu);
//            mediumDAO.actualizar(medium);
//
//            return medium;
//        });
        return null;
    }

    @Override
    public List<Espiritu> espiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina){
//        return HibernateTransactionRunner.runTrx(() -> {
//            if (pagina < 0) {
//                throw new RuntimeException("El número de página " + pagina + " es menor a 0");
//            }
//            return espirituDAO.recuperarDemoniacosPaginados(direccion, pagina, cantidadPorPagina);
//        });
        return null;
    }

}
