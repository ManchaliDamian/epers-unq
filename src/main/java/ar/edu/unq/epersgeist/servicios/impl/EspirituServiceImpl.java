package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
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
    public void guardar(Espiritu espiritu) {
            espirituDAO.save(espiritu);
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return espirituDAO.findById(espirituId);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return espirituDAO.findAll();
    }

    @Override
    public void eliminar(Long espirituId) {
        espirituDAO.deleteById(espirituId);
    }

    @Override
    public void eliminarTodo(){
        espirituDAO.deleteAll();
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        Espiritu espiritu = espirituDAO.findById(espirituId)
                .orElseThrow(() -> new NoSuchElementException("Espiritu no encontrado con id: " + espirituId));
        Medium medium = mediumDAO.findById(mediumId)
                .orElseThrow(() -> new NoSuchElementException("Medium no encontrado con id: " + mediumId));;

        medium.conectarseAEspiritu(espiritu);

        espirituDAO.save(espiritu);
        mediumDAO.save(medium);

        return medium;
    }

    @Override
    public List<Espiritu> espiritusDemoniacos(Direccion dir, int pagina, int cantidadPorPagina){
        if (pagina < 0) {
            throw new IllegalArgumentException("El número de página " + pagina + " es menor a 0");
        }
        if (cantidadPorPagina < 0) {
            throw new IllegalArgumentException("La cantidad por pagina " + cantidadPorPagina + " es menor a 0");
        }

        Sort.Direction sortDirection = dir == Direccion.ASCENDENTE ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, cantidadPorPagina, Sort.by(sortDirection, "nivelDeConexion"));

        return espirituDAO.recuperarDemoniacosPaginados(pageable);
    }

}
