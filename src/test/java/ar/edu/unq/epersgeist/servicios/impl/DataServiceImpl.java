package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;

import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.UbicacionMapper;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DataServiceImpl implements DataService {
    private final UbicacionRepository ubicacionRepository;
    private final MediumRepository mediumRepository;
    private final EspirituRepository espirituRepository;

    public DataServiceImpl(UbicacionRepository ubicacionRepository, MediumRepository mediumRepository, EspirituRepository espirituRepository) {
        this.ubicacionRepository = ubicacionRepository;
        this.mediumRepository = mediumRepository;
        this.espirituRepository = espirituRepository;
    }

    public void eliminarTodo() {
        espirituRepository.deleteAll();
        mediumRepository.deleteAll();
        ubicacionRepository.deleteAll();
    }


    public Optional<Medium> recuperarEliminadoMedium(Long mediumId) {
        return mediumRepository.recuperarEliminado(mediumId);
    }

    public List<Medium> recuperarTodosMediumsEliminados(){
        return mediumRepository.recuperarTodosLosEliminados();
    }

    public Optional<Espiritu> recuperarEliminadoEspiritu(Long id) {
        return espirituRepository.recuperarEliminado(id);
    }

    public List<Espiritu> recuperarTodosLosEspiritusEliminados() {
        return espirituRepository.recuperarTodosLosEliminados();
    }

    public Optional<Ubicacion> recuperarEliminadoUbicacion(Long id) {
        return ubicacionRepository.recuperarEliminado(id);
    }

    public List<Ubicacion> recuperarTodosEliminadosDeUbicacion() {
        return ubicacionRepository.recuperarTodosEliminados();
    }
}
