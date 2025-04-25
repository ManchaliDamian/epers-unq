package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.NombreDeUbicacionRepetido;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionDAO ubicacionDAO;

    public UbicacionServiceImpl(UbicacionDAO ubiDao) {
        this.ubicacionDAO = ubiDao;
    }

    @Override
    public void guardar(Ubicacion ubicacion) {
        try {
            ubicacionDAO.save(ubicacion);
        } catch (DataIntegrityViolationException e) {
            throw new NombreDeUbicacionRepetido(ubicacion.getNombre());
        }
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId) {
        return ubicacionDAO.findById(ubicacionId);
    }

    @Override
    public List<Ubicacion> recuperarTodos() {
        return ubicacionDAO.findAll();
    }

    @Override
    public void eliminar(Ubicacion ubicacion) {
            ubicacionDAO.delete(ubicacion);
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return ubicacionDAO.findEspiritusByUbicacionId(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return ubicacionDAO.findMediumsSinEspiritusByUbicacionId(ubicacionId);
    }

    @Override
    public void clearAll() {
        ubicacionDAO.deleteAll();
    }
}
