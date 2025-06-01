package ar.edu.unq.epersgeist.persistencia.repositories.mappers;


import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.ClosenessResultNeoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.DegreeResultNeoDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.ClosenessResult;
import ar.edu.unq.epersgeist.servicios.interfaces.DegreeResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("centralityMapperImpl")
public class CentralityMapperImpl {

    UbicacionMapper ubiMapper;

    CentralityMapperImpl(UbicacionMapper ubiMapper) {
        this.ubiMapper = ubiMapper;
    }

    public List<ClosenessResult> toDomainListCloseness(List<ClosenessResultNeoDTO> resultNeoDTOList){
        return resultNeoDTOList.stream().map(this::toDomainCloseness).collect(Collectors.toList());
    }

    private ClosenessResult toDomainCloseness(ClosenessResultNeoDTO res) {
        return new ClosenessResult(ubiMapper.toDomain(res.ubicacion()), res.closeness());
    }

    public List<DegreeResult> toDomainListDegree(List<DegreeResultNeoDTO> resultNeoDTOList) {
        return resultNeoDTOList.stream().map(this::toDomainDegree).collect(Collectors.toList());
    }

    private DegreeResult toDomainDegree(DegreeResultNeoDTO res) {
        return new DegreeResult(ubiMapper.toDomain(res.ubicacion()), res.degree());
    }
}
