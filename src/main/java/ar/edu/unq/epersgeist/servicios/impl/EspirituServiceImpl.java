package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.ubicaciones.Direccion;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.exception.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;

    public EspirituServiceImpl(EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public Espiritu guardar(Espiritu espiritu) {
        espirituDAO.save(espiritu);
        return espiritu;
    }

    @Override
    public Espiritu actualizar(Espiritu espiritu){
        return espirituDAO.save(espiritu);
    }

    private Espiritu getEspiritu(Long espirituId) {
        Espiritu espiritu = espirituDAO.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(espirituId));
        if(espiritu.isDeleted()) {
            throw new EspirituNoEncontradoException(espirituId);
        }
        return espiritu;
    }

    private Medium getMedium(Long mediumId) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(() -> new MediumNoEncontradoException(mediumId));
        if(medium.isDeleted()) {
            throw new EspirituNoEncontradoException(mediumId);
        }
        return medium;
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return espirituDAO.findById(espirituId)
                .filter(e -> !e.isDeleted());
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return espirituDAO.recuperarTodos();
    }

    @Override
    public Optional<Espiritu> recuperarEliminado(Long id) {
        return espirituDAO.recuperarEliminado(id);
    }

    @Override
    public List<Espiritu> recuperarTodosLosEliminados() {
        return espirituDAO.recuperarTodosLosEliminados();
    }

    @Override
    public void eliminar(Long espirituId) {
        Espiritu espiritu = this.getEspiritu(espirituId);
        espiritu.setDeleted(true);
        espirituDAO.save(espiritu);
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        Espiritu espiritu = this.getEspiritu(espirituId);

        Medium medium = this.getMedium(mediumId);

        medium.conectarseAEspiritu(espiritu);

        espirituDAO.save(espiritu);
        mediumDAO.save(medium);

        return medium;
    }

    @Override
    public List<Espiritu> espiritusDemoniacos(Direccion dir, int pagina, int cantidadPorPagina){
        if (pagina < 1) {
            throw new IllegalArgumentException("El número de página " + pagina + " es menor a 1");
        }
        if (cantidadPorPagina < 0) {
            throw new IllegalArgumentException("La cantidad por pagina " + cantidadPorPagina + " es menor a 0");
        }

        Sort.Direction sortDirection = dir == Direccion.ASCENDENTE ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina-1, cantidadPorPagina, Sort.by(sortDirection, "nivelDeConexion"));

        return espirituDAO.recuperarDemoniacosPaginados(pageable);
    }

}