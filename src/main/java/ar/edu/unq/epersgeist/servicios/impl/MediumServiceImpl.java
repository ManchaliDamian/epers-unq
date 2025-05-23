package ar.edu.unq.epersgeist.servicios.impl;
import ar.edu.unq.epersgeist.modelo.exception.*;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final MediumRepository mediumRepository;
    private final EspirituRepository espirituRepository;
    private final UbicacionRepository ubicacionRepository;

    public MediumServiceImpl(MediumRepository mediumRepository, EspirituRepository espirituRepository, UbicacionRepository ubicacionRepository) {
        this.mediumRepository = mediumRepository;
        this.espirituRepository = espirituRepository;
        this.ubicacionRepository = ubicacionRepository;
    }


    @Override
    public Medium guardar(Medium unMedium) {
        return mediumRepository.save(unMedium);
    }

    @Override
    public Medium actualizar(Medium unMedium) {
        return mediumRepository.save(unMedium);
    }

    private Medium getMedium(Long mediumId) {
        Medium medium = mediumRepository.recuperar(mediumId).orElseThrow(() -> new MediumNoEncontradoException(mediumId));
        if(medium.isDeleted()) {
            throw new MediumNoEncontradoException(mediumId);
        }
        return medium;
    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {
        return mediumRepository.recuperar(mediumId)
                .filter(e -> !e.isDeleted());
    }

    @Override
    public void eliminar(Long mediumId) {

        Medium medium = this.getMedium(mediumId);
        if (!medium.getEspiritus().isEmpty()) {
            throw new MediumNoEliminableException(mediumId);
        }
        medium.setDeleted(true);
        mediumRepository.save(medium);
    }

    @Override
    public List<Medium> recuperarTodos() {
        return mediumRepository.recuperarTodos();
    }


    @Override
    public void descansar(Long mediumId) {
        Medium medium = this.getMedium(mediumId);
        medium.descansar();
        mediumRepository.save(medium);
    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
        Medium mediumExorcista = this.getMedium(idMediumExorcista);
        Medium mediumAExorcizar = this.getMedium(idMediumAExorcizar);

        List<EspirituAngelical> angeles = espirituRepository.recuperarAngelesDe(idMediumExorcista);
        List<EspirituDemoniaco> demonios = espirituRepository.recuperarDemoniosDe(idMediumAExorcizar);

        mediumExorcista.exorcizarA(angeles, demonios, mediumAExorcizar.getUbicacion());


        mediumRepository.save(mediumExorcista);
        mediumRepository.save(mediumAExorcizar);

        angeles.forEach(espirituRepository::save);
        demonios.forEach(espirituRepository::save);
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return mediumRepository.findEspiritusByMediumId(mediumId);
    }

    @Override
    public List<EspirituAngelical> angeles(Long mediumId) {
        return espirituRepository.recuperarAngelesDe(mediumId);
    }

    @Override
    public List<EspirituDemoniaco> demonios(Long mediumId) {
        return espirituRepository.recuperarDemoniosDe(mediumId);
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {

        Optional<Espiritu> espiritu = espirituRepository.recuperar(espirituId);
        if (espiritu.isEmpty()) {
            throw new EspirituNoEncontradoException(espirituId);
        }
        if(espiritu.get().isDeleted()){
            throw  new EspirituNoEncontradoException(espirituId);
        }

        Medium medium = this.getMedium(mediumId);

        medium.invocarA(espiritu.get());

        mediumRepository.save(medium);
        espirituRepository.save(espiritu.get());

        return espiritu.get();
    }

    @Override
    public void mover(Long mediumId, Long ubicacionId) {
        Medium medium = this.getMedium(mediumId);
        Optional<Ubicacion> ubicacion = ubicacionRepository.recuperar(ubicacionId).filter(e -> !e.isDeleted());
        if (ubicacion.isEmpty()) {
            throw new UbicacionNoEncontradaException(ubicacionId);
        }

        medium.mover(ubicacion.get());
        mediumRepository.save(medium);
    }
}