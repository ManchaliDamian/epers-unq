package ar.edu.unq.epersgeist.servicios.impl;
import ar.edu.unq.epersgeist.modelo.exception.*;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;
import ar.edu.unq.epersgeist.persistencia.repositorys.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;
    private final UbicacionRepository ubicacionDAO;

    public MediumServiceImpl(MediumDAO unMediumDAO, EspirituDAO espirituDAO, UbicacionRepository ubicacionDAO) {
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

    private Medium getMedium(Long mediumId) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(() -> new MediumNoEncontradoException(mediumId));
        if(medium.isDeleted()) {
            throw new MediumNoEncontradoException(mediumId);
        }
        return medium;
    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {
        return mediumDAO.findById(mediumId)
                .filter(e -> !e.isDeleted());
    }

    @Override
    public void eliminar(Long mediumId) {

        Medium medium = this.getMedium(mediumId);
        if (!medium.getEspiritus().isEmpty()) {
            throw new MediumNoEliminableException(mediumId);
        }
        medium.setDeleted(true);
        mediumDAO.save(medium);
    }

    @Override
    public List<Medium> recuperarTodos() {
        return mediumDAO.recuperarTodos();
    }


    @Override
    public void descansar(Long mediumId) {
        Medium medium = this.getMedium(mediumId);
        medium.descansar();
        mediumDAO.save(medium);
    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
        Medium mediumExorcista = this.getMedium(idMediumExorcista);
        Medium mediumAExorcizar = this.getMedium(idMediumAExorcizar);

        List<EspirituAngelical> angeles = espirituDAO.recuperarAngelesDe(idMediumExorcista);
        List<EspirituDemoniaco> demonios = espirituDAO.recuperarDemoniosDe(idMediumAExorcizar);

        mediumExorcista.exorcizarA(angeles, demonios, mediumAExorcizar.getUbicacion());


        mediumDAO.save(mediumExorcista);
        mediumDAO.save(mediumAExorcizar);
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return mediumDAO.findEspiritusByMediumId(mediumId);
    }

    @Override
    public List<EspirituAngelical> angeles(Long mediumId) {
        return espirituDAO.recuperarAngelesDe(mediumId);
    }

    @Override
    public List<EspirituDemoniaco> demonios(Long mediumId) {
        return espirituDAO.recuperarDemoniosDe(mediumId);
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {

        Optional<Espiritu> espiritu = espirituDAO.findById(espirituId);
        if (espiritu.isEmpty()) {
            throw new EspirituNoEncontradoException(espirituId);
        }
        if(espiritu.get().isDeleted()){
            throw  new EspirituNoEncontradoException(espirituId);
        }

        Medium medium = this.getMedium(mediumId);

        medium.invocarA(espiritu.get());

        mediumDAO.save(medium);
        espirituDAO.save(espiritu.get());

        return espiritu.get();
    }

    @Override
    public void mover(Long mediumId, Long ubicacionId) {
        Medium medium = this.getMedium(mediumId);
        Optional<Ubicacion> ubicacion = ubicacionDAO.recuperar(ubicacionId).filter(e -> !e.isDeleted());
        if (ubicacion.isEmpty()) {
            throw new UbicacionNoEncontradaException(ubicacionId);
        }

        medium.mover(ubicacion.get());
        mediumDAO.save(medium);
    }
}