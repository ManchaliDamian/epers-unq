package ar.edu.unq.epersgeist.servicios.impl;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exception.EspirituEliminadoException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.exception.MediumEliminadoException;
import ar.edu.unq.epersgeist.modelo.exception.MediumNoEncontradoException;
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
        Optional<Medium> mediumOptional = mediumDAO.findById(mediumId);
        if (mediumOptional.isEmpty()) {
            throw new MediumNoEncontradoException(mediumId);
        }
        if(mediumOptional.get().isDeleted()){
            throw new MediumEliminadoException(mediumId);
        }
        return mediumOptional;
    }

    @Override
    public void eliminar(Long mediumId) {
        Optional<Medium> mediumOptional = mediumDAO.findById(mediumId);
        if(mediumOptional.isPresent()) {
            Medium medium = mediumOptional.get();
            if(medium.isDeleted()) {
                throw new MediumEliminadoException(mediumId);
            }
            else{
                medium.setDeleted(true);
                mediumDAO.save(mediumOptional.get());
            }
        }
        else{
            throw new MediumNoEncontradoException(mediumId);
        }
    }

    @Override
    public List<Medium> recuperarTodos() {
       return mediumDAO.findAll();
    }


    @Override
    public void descansar(Long mediumId) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(() ->
                new EntityNotFoundException("Medium no encontrado con ID: " + mediumId));

        medium.descansar();

        mediumDAO.save(medium);

    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
            Medium mediumExorcista = mediumDAO.findById(idMediumExorcista).orElseThrow(() -> new MediumNoEncontradoException(idMediumExorcista));
            Medium mediumAExorcizar = mediumDAO.findById(idMediumAExorcizar).orElseThrow(() -> new MediumNoEncontradoException(idMediumAExorcizar));

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
    public Espiritu invocar(Long mediumId, Long espirituId) {

            Optional<Espiritu> espiritu = espirituDAO.findById(espirituId);
            if (espiritu.isEmpty()) {
                throw new EspirituNoEncontradoException(espirituId);
            }
            if(espiritu.get().isDeleted()){
                throw  new EspirituEliminadoException(espirituId);
            }
            Optional<Medium> medium = mediumDAO.findById(mediumId);

            medium.get().invocarA(espiritu.get());

            mediumDAO.save(medium.get());
            espirituDAO.save(espiritu.get());

            return espiritu.get();
    }

    @Override
    public void mover(Long mediumId, Long ubicacionId) {
        Optional<Medium> medium = mediumDAO.findById(mediumId);
        Optional<Ubicacion> ubicacion = ubicacionDAO.findById(ubicacionId);

        medium.get().mover(ubicacion.get());

        mediumDAO.save(medium.get());

    }
}
