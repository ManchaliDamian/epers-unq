package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class MediumServiceImpl implements MediumService {

    private final MediumDAO mediumDAO;

    public MediumServiceImpl(MediumDAO unMediumDAO) {
        this.mediumDAO = unMediumDAO;
    }

    @Override
    public Medium crear(Medium unMedium) {
        return HibernateTransactionRunner.runTrx(() ->
                mediumDAO.crear(unMedium));
    }

    @Override
    public Medium recuperar(Long mediumId) {
        return HibernateTransactionRunner.runTrx(() ->
                mediumDAO.recuperar(mediumId));
    }

    @Override
    public void actualizar(Medium unMedium) {
        HibernateTransactionRunner.runTrx(() -> {
            mediumDAO.actualizar(unMedium);
            return null;
        });
    }

    @Override
    public void eliminar(Long mediumId) {
        HibernateTransactionRunner.runTrx(() -> {
            mediumDAO.eliminar(mediumId);
            return null;
        });
    }

    @Override
    public void eliminarTodo() {
        HibernateTransactionRunner.runTrx(() -> {
            mediumDAO.eliminarTodo();
            return null;
        });
    }

    @Override
    public List<Medium> recuperarTodos() {
        return mediumDAO.recuperarTodos();
    }



    @Override
    public void descansar(Long mediumId) {

    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {

    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return null;
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        return null;
    }
}
