package ar.edu.unq.epersgeist.persistencia.repositorys.interfaces;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UbicacionMapper {

    // Actualiza un UbicacionJPA desde el modelo de dominio
    @Mapping(target = "id", ignore = true) // No pisar el ID
    @Mapping(target = "createdAt", ignore = true)
    void actualizarJPADesdeModelo(Ubicacion origen, @MappingTarget UbicacionJPA destino);

    // Mapea un Ubicacion (modelo de dominio) a UbicacionNeo
    UbicacionNeo aNeo(Ubicacion origen);



//
//    // Conversión manual combinando SQL + NEO
//    default UbicacionDTO aDTO(UbicacionJPA sql, UbicacionNeo neo) {
//        Set<Long> conexiones = neo != null && neo.getConexiones() != null
//                ? neo.getConexiones().stream().map(UbicacionNeo::getId).collect(Collectors.toSet())
//                : Set.of();
//
//        UbicacionDTO base = toDTO(sql); // usa el método generado
//        return new UbicacionDTO(
//                base.id(),
//                base.nombre(),
//                base.flujoDeEnergia(),
//                base.tipo()
//        );
//    }
//
//    // Si querés convertir DTO a modelo (dominio)
//    default Ubicacion desdeDTO(UbicacionDTO dto) {
//        return switch (dto.tipo()) {
//            case SANTUARIO -> new Santuario(dto.nombre(), dto.flujoDeEnergia());
//            case CEMENTERIO -> new Cementerio(dto.nombre(), dto.flujoDeEnergia());
//        };
//    }
}

