package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.NombreDeUbicacionRepetidoException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {
    @Autowired
    private UbicacionRepository ubicacionRepository;


    public UbicacionServiceImpl(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public Ubicacion guardar(Ubicacion ubicacion) {
        try {
            return ubicacionRepository.guardar(ubicacion);
        } catch (DataIntegrityViolationException e) {
            throw new NombreDeUbicacionRepetidoException(ubicacion.getNombre());
        }
    }

    @Override
    public Ubicacion actualizar(Ubicacion ubicacion){
        return ubicacionRepository.actualizar(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId) {
        return ubicacionRepository.recuperar(ubicacionId);
    }


    @Override
    public List<Ubicacion> recuperarTodos() {
        return ubicacionRepository.recuperarTodos();
    }


    @Override
    public List<Cementerio> recuperarCementerios() {
        return ubicacionRepository.recuperarCementerios();
    }


    @Override
    public List<Santuario> recuperarSantuarios() {
        return ubicacionRepository.recuperarSantuarios();
    }

    @Override
    public void eliminar(Long id) {
        ubicacionRepository.eliminar(id);

    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return ubicacionRepository.findEspiritusByUbicacionId(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return ubicacionRepository.findMediumsSinEspiritusByUbicacionId(ubicacionId);
    }
}