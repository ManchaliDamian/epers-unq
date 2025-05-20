package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.mapper.EspirituMapper;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.UbicacionJPA.UbicacionJPA;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;

import ar.edu.unq.epersgeist.mapper.UbicacionMapper;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DataServiceImpl implements DataService {
    private final UbicacionDAOSQL ubicacionSQL;
    private final UbicacionDAONeo ubiNeo;
    private final MediumDAO mediumDAO;
    private final EspirituDAO espirituDAO;
    @Autowired
    private UbicacionMapper ubicacionMapper;
    @Autowired
    private EspirituMapper espirituMapper;
    public DataServiceImpl(UbicacionDAOSQL ubicacionDAO, UbicacionDAONeo ubiNeo, MediumDAO mediumDAO, EspirituDAO espirituDAO) {
        this.ubiNeo = ubiNeo;
        this.ubicacionSQL = ubicacionDAO;
        this.mediumDAO = mediumDAO;
        this.espirituDAO = espirituDAO;
    }

    public void eliminarTodo() {
        espirituDAO.deleteAll();
        mediumDAO.deleteAll();
        ubicacionSQL.deleteAll();
        ubiNeo.deleteAll();
    }


    public Optional<Medium> recuperarEliminadoMedium(Long mediumId) {
        return mediumDAO.recuperarEliminado(mediumId);
    }

    public List<Medium> recuperarTodosMediumsEliminados(){
        return mediumDAO.recuperarTodosEliminados();
    }

    public Optional<Espiritu> recuperarEliminadoEspiritu(Long id) {
        return espirituMapper.toModel(espirituDAO.recuperarEliminado(id));
    }

    public List<Espiritu> recuperarTodosLosEspiritusEliminados() {
        return this.espirituMapper.toModelList(espirituDAO.recuperarTodosLosEliminados());
    }

    public Optional<Ubicacion> recuperarEliminadoUbicacion(Long id) {
        return ubicacionSQL.recuperarEliminado(id)
                .map(ubicacionMapper::aModelo);

    }

    public List<Ubicacion> recuperarTodosEliminadosDeUbicacion() {
        List<UbicacionJPA> ubicacionesEliminadas = ubicacionSQL.recuperarTodosEliminados();

        return ubicacionesEliminadas.stream()
                .map(ubicacionMapper::aModelo)
                .toList();
    }
}
