package ar.edu.unq.epersgeist.servicios.impl;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituEliminado;
import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituNoEncontrado;

import ar.edu.unq.epersgeist.modelo.exception.MediumEliminadoException;
import ar.edu.unq.epersgeist.modelo.exception.MediumNoEncontrado;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;
    private final UbicacionDAO ubicacionDAO;

    public MediumServiceImpl(MediumDAO unMediumDAO, EspirituDAO espirituDAO, UbicacionDAO ubicacionDAO) {
        this.mediumDAO = unMediumDAO;
        this.espirituDAO = espirituDAO;
        this.ubicacionDAO = ubicacionDAO;
    }


    @Override
    public Medium guardar(Medium unMedium) {
        return mediumDAO.save(unMedium);
    }

    @Override
    public Medium actualizar(Medium unMedium) {
        return mediumDAO.save(unMedium);
    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {
        Optional<Medium> mediumARecuperar = mediumDAO.findById(mediumId).filter(e -> !e.isDeleted());
        if (mediumARecuperar.isEmpty()) {
           throw new MediumNoEncontrado(mediumId);
        }
        return mediumARecuperar;
    }
    @Override
    public Optional<Medium> recuperarEliminado(Long mediumId) {
        Optional<Medium> mediumARecuperar = mediumDAO.recuperarEliminado(mediumId);
        if (mediumARecuperar.isEmpty()) {
            throw new MediumNoEncontrado(mediumId);
        }
        return mediumARecuperar;
    }
    @Override
    public List<Medium> recuperarTodosEliminados(){
        return mediumDAO.recuperarTodosEliminados();
    }
    @Override
    public void eliminar(Long mediumId) {
        Optional<Medium> mediumEliminadoLogico = this.recuperar(mediumId);
        mediumEliminadoLogico.get().setDeleted(true);
        mediumDAO.save(mediumEliminadoLogico.get());

    }

    @Override
    public List<Medium> recuperarTodos() {
       return mediumDAO.recuperarTodos();
    }


    @Override
    public void descansar(Long mediumId) {
        Optional<Medium> medium = this.recuperar(mediumId);

        medium.get().descansar();

        mediumDAO.save(medium.get());

    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
            Optional<Medium> mediumExorcista = this.recuperar(idMediumExorcista);
            Optional<Medium> mediumAExorcizar = this.recuperar(idMediumAExorcizar);

            List<EspirituAngelical> angeles = espirituDAO.recuperarAngelesDe(mediumExorcista.get().getId());
            List<EspirituDemoniaco> demonios = espirituDAO.recuperarDemoniosDe(mediumAExorcizar.get().getId());


            mediumExorcista.get().exorcizarA(angeles, demonios, mediumAExorcizar.get().getUbicacion());


            mediumDAO.save(mediumExorcista.get());
            mediumDAO.save(mediumAExorcizar.get());


    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return mediumDAO.findEspiritusByMediumId(mediumId);
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {

            Optional<Espiritu> espiritu = espirituDAO.findById(espirituId).filter(e -> !e.isDeleted());
            if (espiritu.isEmpty()) {
                throw new ExceptionEspirituNoEncontrado(espirituId);
            }

            Optional<Medium> medium = this.recuperar(mediumId);

            medium.get().invocarA(espiritu.get());

            mediumDAO.save(medium.get());
            espirituDAO.save(espiritu.get());

            return espiritu.get();
    }

    @Override
    public void mover(Long mediumId, Long ubicacionId) {
        Optional<Medium> medium = this.recuperar(mediumId);
        Optional<Ubicacion> ubicacion = ubicacionDAO.findById(ubicacionId);

        medium.get().mover(ubicacion.get());

        mediumDAO.save(medium.get());

    }
}
