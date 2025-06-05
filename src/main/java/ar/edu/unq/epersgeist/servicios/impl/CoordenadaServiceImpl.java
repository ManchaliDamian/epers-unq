package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.CoordenadaRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.CoordenadaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class CoordenadaServiceImpl implements CoordenadaService {

    private final CoordenadaRepository coordenadaRepository;

    public CoordenadaServiceImpl(CoordenadaRepository coordenadaRepository) {
        this.coordenadaRepository = coordenadaRepository;
    }

    @Override
    public void guardar(Coordenada dominio, Long entidadId, TipoDeEntidad tipo) {
        coordenadaRepository.guardar(dominio, entidadId, tipo);
    }

    @Override
    public Optional<Coordenada> recuperar(Long entidadId, TipoDeEntidad tipo) {
        return coordenadaRepository.recuperar(entidadId, tipo);
    }

    @Override
    public void eliminar(Long entidadId, TipoDeEntidad tipo) {
        coordenadaRepository.eliminar(entidadId, tipo);
    }
}