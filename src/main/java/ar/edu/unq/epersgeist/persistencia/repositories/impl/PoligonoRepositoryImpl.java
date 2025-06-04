package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import ar.edu.unq.epersgeist.persistencia.DAOs.PoligonoDAO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.PoligonoRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.PoligonoMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Repository
public class PoligonoRepositoryImpl implements PoligonoRepository {

    private PoligonoMapper poligonoMapper;
    private PoligonoDAO poligonoDAO;

    public PoligonoRepositoryImpl(PoligonoDAO poligonoDAO, PoligonoMapper poligonoMapper) {
        this.poligonoMapper = poligonoMapper;
        this.poligonoDAO = poligonoDAO;
    }

    @Override
    public void guardar(Long ubicacionId, Poligono poligono) {
        PoligonoMongoDTO poligonoMongo = poligonoMapper.toMongo(ubicacionId, poligono);
        this.poligonoDAO.save(poligonoMongo);
    }

    @Override
    public void actualizar(Poligono poligono) {
        //hacer
    }

    @Override
    public List<Poligono> recuperarTodos() {
        return poligonoDAO.findAll().stream()
                .map(poligonoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Poligono> recuperar(String poligonoId) {
        return poligonoDAO.findById(poligonoId)
                .map(poligonoMapper::toDomain);
    }

    @Override
    public Optional<Poligono> recuperarPorUbicacionId(Long ubicacionId) {
        return poligonoDAO.findByUbicacionId(ubicacionId)
                .map(poligonoMapper::toDomain);
    }

    @Override
    public void eliminarPorUbicacionId(Long ubicacionId) {
        poligonoDAO.deleteByUbicacionId(ubicacionId);
    }

    @Override
    public void deleteAll() {
        poligonoDAO.deleteAll();
    }
}
