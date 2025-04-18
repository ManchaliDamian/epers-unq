package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
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

            List<EspirituAngelical> angeles = espirituDAO.recuperarAngelesDe(idMediumExorcista);
            List<EspirituDemoniaco> demonios = espirituDAO.recuperarDemoniosDe(idMediumAExorcizar);

            //-------esta mal una exception aca? ------------------------
//            if (angeles.isEmpty()){
//                throw new ExorcistaSinAngelesException(mediumExorcista);
//            }
            //---------------------------------------------------------------------------------------
            mediumExorcista.exorcizarA(angeles, demonios);

            mediumDAO.actualizar(mediumExorcista);
            mediumDAO.actualizar(mediumAExorcizar);
            return null;
        });
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return HibernateTransactionRunner.runTrx(() ->
            mediumDAO.espiritus(mediumId));
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        return HibernateTransactionRunner.runTrx(() -> {
            Espiritu espiritu = espirituDAO.recuperar(espirituId);
            Medium medium = mediumDAO.recuperar(mediumId);

            medium.invocarA(espiritu);

            mediumDAO.actualizar(medium);
            espirituDAO.actualizar(espiritu);


            return espiritu;

        });

    }
}
