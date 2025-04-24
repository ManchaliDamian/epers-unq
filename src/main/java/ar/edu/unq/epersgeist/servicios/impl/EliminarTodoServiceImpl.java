package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

public class EliminarTodoServiceImpl {
    private final UbicacionDAO ubicacionDAO;
    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;


    public EliminarTodoServiceImpl(UbicacionDAO ubicacionDAO, MediumDAO mediumDAO, EspirituDAO espirituDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.mediumDAO = mediumDAO;
        this.espirituDAO = espirituDAO;
    }
    public void eliminarTodo() {
        HibernateTransactionRunner.runTrx(() -> {
            espirituDAO.deleteAll();
            mediumDAO.deleteAll();
            ubicacionDAO.deleteAll();
            return null;
        });
    }
}
