package ar.edu.unq.epersgeist.persistencia.repositorys.interfaces;

import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UbicacionMapper {

    // Conversión automática entre JPA y DTO
    UbicacionDTO toDTO(UbicacionJPA ubicacionJPA);

    UbicacionJPA toJPA(UbicacionDTO dto); // opcional, solo si necesitás convertir de vuelta



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

