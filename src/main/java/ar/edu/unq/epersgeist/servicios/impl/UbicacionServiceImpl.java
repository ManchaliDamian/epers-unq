package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class UbicacionServiceImpl implements UbicacionService {

    @Override
    public Ubicacion crear(Ubicacion ubicacion) {
        return null;

    }

    @Override
    public Ubicacion recuperar(Long ubicacionId) {
        return null;
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return List.of();
    }

    @Override
    public void actualizar(Ubicacion ubicacion) {

    }

    @Override
    public void eliminar(Long ubicacionId) {

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
