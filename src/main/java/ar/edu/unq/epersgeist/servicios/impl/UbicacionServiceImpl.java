package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.exception.NombreDeUbicacionRepetido;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionDAO ubicacionDAO;

    public UbicacionServiceImpl(UbicacionDAO ubiDao) {
        this.ubicacionDAO = ubiDao;
    }

    @Override
    public Ubicacion guardar(Ubicacion ubicacion) {
        try {
            return ubicacionDAO.save(ubicacion);
        } catch (DataIntegrityViolationException e) {
            throw new NombreDeUbicacionRepetido(ubicacion.getNombre());
        }
    }

    @Override
    public Ubicacion actualizar(Ubicacion ubicacion){
        return ubicacionDAO.save(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId) {
        Optional<Ubicacion> ubicacionARecuperar = ubicacionDAO.findById(ubicacionId).filter(u -> !u.isDeleted());
        if (ubicacionARecuperar.isEmpty()) {
            throw new MediumNoEncontradoException(ubicacionId);
        }
        return ubicacionARecuperar;
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return ubicacionDAO.recuperarTodos();
    }


    @Override
    public void eliminar(Long id) {
        Optional<Ubicacion> ubicacionAEliminar = this.recuperar(id);
        ubicacionAEliminar.get().setDeleted(true);
        ubicacionDAO.save(ubicacionAEliminar.get());
    }
    @Override
    public Optional<Ubicacion> recuperarEliminado(Long id) {
        Optional<Ubicacion> ubicacionARecuperar = ubicacionDAO.recuperarEliminado(id);
        if (ubicacionARecuperar.isEmpty()) {
            throw new UbicacionNoEncontradaException(id);
        }
        return ubicacionARecuperar;
    }
    @Override
    public List<Ubicacion> recuperarTodosEliminados() {
        return ubicacionDAO.recuperarTodosEliminados();
    }


    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return ubicacionDAO.findEspiritusByUbicacionId(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return ubicacionDAO.findMediumsSinEspiritusByUbicacionId(ubicacionId);
    }
}
