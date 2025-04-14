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
    public UbicacionServiceImpl(UbicacionDAO ubiDao) {
        this.ubicacionDAO = ubiDao;
    }
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
        return HibernateTransactionRunner.runTrx(() -> ubicacionDAO.recuperarTodos());
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {
        HibernateTransactionRunner.runTrx(() -> {
            ubicacionDAO.actualizar(ubicacion);
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
        return HibernateTransactionRunner.runTrx(() -> ubicacionDAO.espiritusEn(ubicacionId));
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return HibernateTransactionRunner.runTrx(() -> ubicacionDAO.mediumsSinEspiritusEn(ubicacionId));
    }

    public void eliminarTodo() {
        HibernateTransactionRunner.runTrx(() -> {
            ubicacionDAO.eliminarTodo();
            return null;
        });
    }
}
