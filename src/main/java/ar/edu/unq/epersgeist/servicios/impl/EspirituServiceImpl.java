package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;

    public EspirituServiceImpl(EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public void guardar(Espiritu espiritu) {
        HibernateTransactionRunner.runTrx(() -> {
            espirituDAO.guardar(espiritu);
            return null;
        });
    }

    @Override
    public Espiritu recuperar(Long espirituId) {
        return HibernateTransactionRunner.runTrx(() ->
                espirituDAO.recuperar(espirituId));
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return HibernateTransactionRunner.runTrx(() -> espirituDAO.recuperarTodos());
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        HibernateTransactionRunner.runTrx(() -> {
            espirituDAO.actualizar(espiritu);
            return null;
        });
    }

    @Override
    public void eliminar(Long espirituId) {
        HibernateTransactionRunner.runTrx(() -> {
            Espiritu espiritu = espirituDAO.recuperar(espirituId);
            espirituDAO.eliminar(espiritu);
            return null;
        });
    }

    @Override
    public void eliminarTodo() {
        HibernateTransactionRunner.runTrx(() -> {
            espirituDAO.eliminarTodo();
            mediumDAO.eliminarTodo();
            return null;
        });
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        return HibernateTransactionRunner.runTrx(() -> {
            Espiritu espiritu = espirituDAO.recuperar(espirituId);
            Medium medium = mediumDAO.recuperar(mediumId);

            medium.conectarseAEspiritu(espiritu);
            espiritu.aumentarConexion(medium);

            espirituDAO.actualizar(espiritu);
            mediumDAO.actualizar(medium);

            return medium;
        });
    }

    @Override
    public List<Espiritu> espiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina){
        return HibernateTransactionRunner.runTrx(() -> {
            if (pagina < 0) {
                throw new RuntimeException("El número de página " + pagina + " es menor a 0");
            }
            return espirituDAO.recuperarDemoniacosPaginados(direccion, pagina, cantidadPorPagina);
        });
    }

}
