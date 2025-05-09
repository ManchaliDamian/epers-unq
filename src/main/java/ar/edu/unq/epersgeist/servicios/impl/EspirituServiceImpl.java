package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
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
import java.util.NoSuchElementException;
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
    public void actualizar(Espiritu espiritu){
        espirituDAO.save(espiritu);
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        Optional<Espiritu> espirituARecuperar = espirituDAO.findById(espirituId).filter(e -> !e.isDeleted());
        if (espirituARecuperar.isEmpty()) {
            throw new EspirituNoEncontradoException(espirituId);
        }

        return espirituARecuperar;
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return espirituDAO.recuperarTodos();
    }
    @Override
    public Optional<Espiritu> recuperarEliminado(Long id) {
        Optional<Espiritu> espirituARecuperarEliminado = espirituDAO.recuperarEliminado(id);
        if (espirituARecuperarEliminado.isEmpty()) {
            throw new EspirituNoEncontradoException(id);
        }
        return espirituARecuperarEliminado;
    }
    @Override
    public List<Espiritu> recuperarTodosLosEliminados() {
        return espirituDAO.recuperarTodosLosEliminados();
    }
    @Override
    public void eliminar(Long espirituId) {
        Optional<Espiritu> espirituEliminadoLogico = this.recuperar(espirituId);
        espirituEliminadoLogico.get().setDeleted(true);
        espirituDAO.save(espirituEliminadoLogico.get());
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        Optional<Espiritu> espiritu = this.recuperar(espirituId);

        Optional<Medium> medium = mediumDAO.findById(mediumId).filter(e -> !e.isDeleted());
        if (medium.isEmpty()) {
            throw new MediumNoEncontradoException(mediumId);
        }

        medium.get().conectarseAEspiritu(espiritu.get());

        espirituDAO.save(espiritu.get());
        mediumDAO.save(medium.get());

        return medium.get();
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