package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;

import java.util.Optional;

public interface CoordenadaService {

    void guardar(Coordenada dominio, Long entidadId, TipoDeEntidad tipo);

    Optional<Coordenada> recuperar(Long entidadId, TipoDeEntidad tipo);

    void eliminar(Long entidadId, TipoDeEntidad tipo);
}

