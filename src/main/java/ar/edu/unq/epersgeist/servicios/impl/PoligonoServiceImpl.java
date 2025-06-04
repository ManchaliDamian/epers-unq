package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.PoligonoRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.PoligonoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PoligonoServiceImpl implements PoligonoService {

    private final PoligonoRepository poligonoRepository;

    public PoligonoServiceImpl(PoligonoRepository poligonoRepository) {
        this.poligonoRepository = poligonoRepository;
    }

    @Override
    public void guardar(Long ubicacionId, Poligono poligono) {
        poligonoRepository.save(ubicacionId, poligono);
    }

    @Override
    public void actualizar(Poligono poligono) {
        poligonoRepository.save(poligono);
    }

    @Override
    public Optional<Poligono> recuperar(String poligonoId) {
        return poligonoRepository.recuperar(poligonoId);
    }

    @Override
    public Optional<Poligono> recuperarPorUbicacionId(Long ubicacionId) {
        return poligonoRepository.recuperarPorUbicacionId(ubicacionId);
    }

    @Override
    public List<Poligono> recuperarTodos() {
        return poligonoRepository.recuperarTodos();
    }

    @Override
    public void eliminar(Long ubicacionId) {
        poligonoRepository.eliminarPorUbicacionId(ubicacionId);
    }

    @Override
    public void eliminarTodos() {
        poligonoRepository.deleteAll();
    }
}