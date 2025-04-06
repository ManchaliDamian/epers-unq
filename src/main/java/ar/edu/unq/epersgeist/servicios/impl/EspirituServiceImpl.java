package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.jdbc.JDBCEspirituDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAO espirituDAO;

    public EspirituServiceImpl(EspirituDAO espirituDAO) {
        this.espirituDAO = espirituDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {
        return HibernateTransactionRunner.runTrx(() ->
            espirituDAO.crear(espiritu));
    }

    @Override
    public Espiritu recuperar(Long espirituId) {
        return HibernateTransactionRunner.runTrx(() ->
                espirituDAO.recuperar(espirituId));
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return espirituDAO.recuperarTodos();
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
            espirituDAO.eliminar(espirituId);
            return null;
        });
    }

    @Override
    public Medium conectar(Long espirituId, Medium medium) {
        return HibernateTransactionRunner.runTrx(() -> {
            Espiritu currentEspiritu = espirituDAO.recuperar(espirituId);
            medium.conectarseAEspiritu(currentEspiritu);
            currentEspiritu.aumentarConexion(medium);

            espirituDAO.actualizar(currentEspiritu);

            return medium;
        });
    }
}
