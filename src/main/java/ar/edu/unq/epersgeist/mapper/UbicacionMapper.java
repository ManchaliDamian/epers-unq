package ar.edu.unq.epersgeist.mapper;

import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.persistencia.UbicacionJPA.CementerioJPA;
import ar.edu.unq.epersgeist.persistencia.UbicacionJPA.SantuarioJPA;
import ar.edu.unq.epersgeist.persistencia.UbicacionJPA.UbicacionJPA;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UbicacionMapper {

    // Actualiza un UbicacionJPA desde el modelo de dominio
    @Mapping(target = "id", ignore = true) // No pisar el ID
    @Mapping(target = "createdAt", ignore = true)
    void actualizarJPADesdeModelo(Ubicacion origen, @MappingTarget UbicacionJPA destino);
    Santuario aModelo(SantuarioJPA jpa);
    Cementerio aModelo(CementerioJPA jpa);




    default UbicacionJPA toJpa(Ubicacion ubicacion) {
        if (ubicacion == null) {
            return null;
        }
        if (ubicacion instanceof Santuario) {
            return santuarioToJpa((Santuario) ubicacion);
        } else if (ubicacion instanceof Cementerio) {
            return cementerioToJpa((Cementerio) ubicacion);
        } else {
            throw new IllegalArgumentException("Tipo de Ubicación no soportado: " + ubicacion.getClass().getName());
        }
    }

    SantuarioJPA santuarioToJpa(Santuario santuario);
    CementerioJPA cementerioToJpa(Cementerio cementerio);

    default List<Ubicacion> toModelListUbicacion(List<UbicacionJPA> lista) {
        return lista.stream().map(this::aModelo).collect(Collectors.toList());
    }

    List<Cementerio> toModelListCementerio(List<CementerioJPA> cementerios);
    List<Santuario> toModelListSantuarios(List<SantuarioJPA> santuarios);

    default Ubicacion aModelo(UbicacionJPA jpa) {
        return switch (jpa.getTipo()) {
            case SANTUARIO -> {
                Santuario santuario = new Santuario(jpa.getNombre(), jpa.getFlujoDeEnergia());
                santuario.setId(jpa.getId());
                yield santuario;
            }
            case CEMENTERIO -> {
                Cementerio cementerio = new Cementerio(jpa.getNombre(), jpa.getFlujoDeEnergia());
                cementerio.setId(jpa.getId());
                yield cementerio;
            }
        };
    }

}

