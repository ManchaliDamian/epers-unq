package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituOcupado;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;

import java.util.List;

public class MediumServiceImpl implements MediumService {

    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;

    public MediumServiceImpl(MediumDAO unMediumDAO, EspirituDAO espirituDAO) {
        this.mediumDAO = unMediumDAO;
        this.espirituDAO = espirituDAO;
    }

    private EspirituServiceImpl espirituService;

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
        return HibernateTransactionRunner.runTrx(mediumDAO::recuperarTodos);
    }



    @Override
    public void descansar(Long mediumId) {
        HibernateTransactionRunner.runTrx(() -> {
            Medium medium = mediumDAO.recuperar(mediumId);

            medium.descansar();

            mediumDAO.actualizar(medium);
            return null;
        });
    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
        HibernateTransactionRunner.runTrx(() -> {
            Medium mediumExorcista = mediumDAO.recuperar(idMediumExorcista);
            Medium mediumAExorcizar = mediumDAO.recuperar(idMediumAExorcizar);

            mediumExorcista.exorcizarA(mediumAExorcizar);

            mediumDAO.actualizar(mediumExorcista);
            mediumDAO.actualizar(mediumAExorcizar);
            return null;
        });
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return mediumDAO.espiritus(mediumId);
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        Espiritu espiritu = espirituService.recuperar(espirituId);
        Medium medium = mediumDAO.recuperar(mediumId);

        if (espiritu.estaConectado()) throw new ExceptionEspirituOcupado(espiritu);
        if (medium.getMana() < 10) return espiritu;

        espiritu.setUbicacion(medium.getUbicacion());
        medium.setMana(medium.getMana() - 10);

        return espiritu;

    }
}
