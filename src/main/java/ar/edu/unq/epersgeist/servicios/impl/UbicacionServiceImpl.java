package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEliminableException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.NombreDeUbicacionRepetidoException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.persistencia.UbicacionDAO;
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
            throw new NombreDeUbicacionRepetidoException(ubicacion.getNombre());
        }
    }

    @Override
    public Ubicacion actualizar(Ubicacion ubicacion){
        return ubicacionDAO.save(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId) {
        return ubicacionDAO.findById(ubicacionId).filter(u -> !u.isDeleted());
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return ubicacionDAO.recuperarTodos();
    }

    @Override
    public List<Cementerio> recuperarCementerios() {
        return ubicacionDAO.recuperarCementerios();
    }

    @Override
    public List<Santuario> recuperarSantuarios() {
        return ubicacionDAO.recuperarSantuarios();
    }

    @Override
    public void eliminar(Long id) {
        if (!ubicacionDAO.findEspiritusByUbicacionId(id).isEmpty() || !ubicacionDAO.findMediumByUbicacionId(id).isEmpty()) {
            throw new UbicacionNoEliminableException(id);
        }
        Ubicacion ubicacionAEliminar = this.recuperar(id).orElseThrow(() -> new UbicacionNoEncontradaException(id));
        ubicacionAEliminar.setDeleted(true);
        ubicacionDAO.save(ubicacionAEliminar);
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