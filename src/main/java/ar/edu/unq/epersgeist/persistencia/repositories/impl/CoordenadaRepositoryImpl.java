package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DAOs.CoordenadaDAO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.CoordenadaMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.CoordenadaRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.CoordenadaMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CoordenadaRepositoryImpl implements CoordenadaRepository {
    private final CoordenadaDAO coordenadaDAO;
    private final CoordenadaMapper coordenadaMapper;

    public CoordenadaRepositoryImpl(CoordenadaDAO dao, CoordenadaMapper mapper) {
        this.coordenadaDAO = dao;
        this.coordenadaMapper = mapper;
    }

    @Override
    public void guardar(Coordenada dominio, Long entidadId, TipoDeEntidad tipo) {
        //si existe lo actualiza, no crea uno nuevo, ni lanza exception
        CoordenadaMongoDTO existente = coordenadaDAO.findByEntidadIdAndTipoDeEntidad(entidadId, tipo)
                .orElse(null);

        CoordenadaMongoDTO dto = coordenadaMapper.toMongo(dominio, entidadId, tipo);
        if (existente != null) {
            dto.setId(existente.getId());
        }
        coordenadaDAO.save(dto);
    }

    @Override
    public Optional<Coordenada> recuperar(Long entidadId, TipoDeEntidad tipo) {
        return coordenadaDAO.findByEntidadIdAndTipoDeEntidad(entidadId, tipo)
                .map(coordenadaMapper::toDomain);
    }

    @Override
    public void eliminar(Long entidadId, TipoDeEntidad tipo) {
        coordenadaDAO.findByEntidadIdAndTipoDeEntidad(entidadId, tipo)
                .ifPresent(dto -> coordenadaDAO.deleteById(dto.getId()));
    }
}

