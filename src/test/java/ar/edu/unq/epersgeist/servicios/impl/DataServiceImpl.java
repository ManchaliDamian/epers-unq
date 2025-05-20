package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;

import ar.edu.unq.epersgeist.persistencia.repositories.mappers.UbicacionMapper;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DataServiceImpl implements DataService {
    private final UbicacionDAOSQL ubicacionSQL;
    private final UbicacionDAONeo ubiNeo;
    private final MediumDAO mediumRepository;
    private final EspirituDAO espirituDAO;
    private UbicacionMapper ubicacionMapper;
    public DataServiceImpl(UbicacionDAOSQL ubicacionDAO, UbicacionDAONeo ubiNeo, MediumDAO mediumRepository, EspirituDAO espirituDAO) {
        this.ubiNeo = ubiNeo;
        this.ubicacionSQL = ubicacionDAO;
        this.mediumRepository = mediumRepository;
        this.espirituDAO = espirituDAO;
    }

    public void eliminarTodo() {
        espirituDAO.deleteAll();
        mediumRepository.deleteAll();
        ubicacionSQL.deleteAll();
        ubiNeo.deleteAll();
    }


    public Optional<Medium> recuperarEliminadoMedium(Long mediumId) {
        return mediumRepository.recuperarEliminado(mediumId);
    }

    public List<Medium> recuperarTodosMediumsEliminados(){
        return mediumRepository.recuperarTodosEliminados();
    }

    public Optional<Espiritu> recuperarEliminadoEspiritu(Long id) {
        return espirituDAO.recuperarEliminado(id);
    }

    public List<Espiritu> recuperarTodosLosEspiritusEliminados() {
        return espirituDAO.recuperarTodosLosEliminados();
    }

    public Optional<Ubicacion> recuperarEliminadoUbicacion(Long id) {
        return ubicacionSQL.recuperarEliminado(id)
                .map(ubicacionMapper::toDomain);

    }

    public List<Ubicacion> recuperarTodosEliminadosDeUbicacion() {
        List<UbicacionJPADTO> ubicacionesEliminadas = ubicacionSQL.recuperarTodosEliminados();

        return ubicacionesEliminadas.stream()
                .map(ubicacionMapper::toDomain)
                .toList();
    }
}
