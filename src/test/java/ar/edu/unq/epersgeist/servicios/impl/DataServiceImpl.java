package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.persistencia.DAOs.*;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Ubicacion;

import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DataServiceImpl implements DataService {
    private final UbicacionDAO ubicacionDAO;
    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;

    public DataServiceImpl(UbicacionDAO ubicacionDAO, MediumDAO mediumDAO, EspirituDAO espirituDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.mediumDAO = mediumDAO;
        this.espirituDAO = espirituDAO;
    }

    public void eliminarTodo() {
        espirituDAO.deleteAll();
        mediumDAO.deleteAll();
        ubicacionDAO.deleteAll();
    }


    public Optional<Medium> recuperarEliminadoMedium(Long mediumId) {
        return mediumDAO.recuperarEliminado(mediumId);
    }

    public List<Medium> recuperarTodosMediumsEliminados(){
        return mediumDAO.recuperarTodosEliminados();
    }

    public Optional<Espiritu> recuperarEliminadoEspiritu(Long id) {
        return espirituDAO.recuperarEliminado(id);
    }

    public List<Espiritu> recuperarTodosLosEspiritusEliminados() {
        return espirituDAO.recuperarTodosLosEliminados();
    }

    public Optional<Ubicacion> recuperarEliminadoUbicacion(Long id) {
        return ubicacionDAO.recuperarEliminado(id);
    }


    public List<Ubicacion> recuperarTodosEliminadosDeUbicacion() {
        return ubicacionDAO.recuperarTodosEliminados();
    }
}
