package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.ClosenessResultNeoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.DegreeResultNeoDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.ClosenessResult;
import ar.edu.unq.epersgeist.servicios.interfaces.DegreeResult;

import java.util.List;

public interface CentralityMapper {
    List<ClosenessResult> toDomainListCloseness(List<ClosenessResultNeoDTO> resultNeoDTOList);

    List<DegreeResult> toDomainListDegree(List<DegreeResultNeoDTO> resultNeoDTOList);
}
