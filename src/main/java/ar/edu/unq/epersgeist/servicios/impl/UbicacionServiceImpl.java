package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class UbicacionServiceImpl implements UbicacionService {

    private UbicacionDAO ubicacionDAO;
    @Override
    public void crear(Ubicacion ubicacion) {
        HibernateTransactionRunner.runTrx(() -> {
            ubicacionDAO.guardar(ubicacion);
            return null;
        });
    }

    @Override
    public Ubicacion recuperar(Long ubicacionId) {
        return HibernateTransactionRunner.runTrx(() -> ubicacionDAO.recuperar(ubicacionId));
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return List.of();
    }

    @Override
    public void actualizar(Long ubicacionId, String nombreNuevo) {
        HibernateTransactionRunner.runTrx(() -> {
            ubicacionDAO.actualizar(ubicacionId, nombreNuevo);
            return null;
        });
    }

    @Override
    public void eliminar(Ubicacion ubicacion) {
        HibernateTransactionRunner.runTrx(() -> {
            ubicacionDAO.eliminar(ubicacion);
            return null;
        });
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return List.of();
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return List.of();
    }
}
