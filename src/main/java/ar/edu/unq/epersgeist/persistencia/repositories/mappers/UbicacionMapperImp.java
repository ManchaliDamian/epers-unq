package ar.edu.unq.epersgeist.persistencia.repositories.mappers;

import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.CementerioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.SantuarioJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Component("ubicacionMapperImpl")
public class UbicacionMapperImp implements UbicacionMapper{

    @Override
    public Santuario toDomainSantuario(SantuarioJPADTO jpa) {
        Santuario santuario = new Santuario(jpa.getNombre(), jpa.getFlujoDeEnergia());
        santuario.setId(jpa.getId());
        santuario.setTipo(jpa.getTipo());
        santuario.setDeleted(jpa.isDeleted());
        santuario.setCreatedAt(jpa.getCreatedAt());
        santuario.setUpdatedAt(jpa.getUpdatedAt());
        return santuario;
    }

    @Override
    public Cementerio toDomainCementerio(CementerioJPADTO jpa) {
        Cementerio cementerio = new Cementerio(jpa.getNombre(), jpa.getFlujoDeEnergia());
        cementerio.setId(jpa.getId());
        cementerio.setTipo(jpa.getTipo());
        cementerio.setDeleted(jpa.isDeleted());
        cementerio.setCreatedAt(jpa.getCreatedAt());
        cementerio.setUpdatedAt(jpa.getUpdatedAt());
        return cementerio;
    }

    @Override
    public Ubicacion toDomain(UbicacionJPADTO jpa) {
        TipoUbicacion tipo = jpa.getTipo();
        return switch (tipo){
            case CEMENTERIO -> toDomainCementerio((CementerioJPADTO) jpa);
            case SANTUARIO -> toDomainSantuario((SantuarioJPADTO) jpa);
        };
    }

    @Override
    public Ubicacion toDomain(UbicacionNeoDTO ubicacion) {
        TipoUbicacion tipo = ubicacion.getTipo();
        Ubicacion ubi;
        if (tipo == TipoUbicacion.CEMENTERIO){
            ubi = new Cementerio();
        } else {
            ubi = new Santuario();
        }
        ubi.setId(ubicacion.getId());
        ubi.setTipo(tipo);
        ubi.setDeleted(ubicacion.isDeleted());
        return ubi;
    }


    @Override
    public List<Ubicacion> toDomainList(List<UbicacionJPADTO> ubicacionJPADTOS) {
        if (ubicacionJPADTOS == null || ubicacionJPADTOS.isEmpty()) {
            return Collections.emptyList();
        }
        return ubicacionJPADTOS.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cementerio> toDomainListCementerio(List<CementerioJPADTO> cementerios) {
        if (cementerios == null || cementerios.isEmpty()) {
            return Collections.emptyList();
        }
        return cementerios.stream()
                .map(this::toDomainCementerio)
                .collect(Collectors.toList());
    }

    @Override
    public List<Santuario> toDomainListSantuarios(List<SantuarioJPADTO> santuarios) {
        if (santuarios == null || santuarios.isEmpty()) {
            return Collections.emptyList();
        }
        return santuarios.stream()
                .map(this::toDomainSantuario)
                .collect(Collectors.toList());
    }

    @Override
    public UbicacionJPADTO toJpa(Ubicacion ubicacion) {
        if (ubicacion == null) {
            return null;
        }
        return switch (ubicacion) {
            case Cementerio c -> toJpa(c);
            case Santuario s -> toJpa(s);
            default -> throw new IllegalStateException("Tipo de Ubicacion desconocido para toJpa: " + ubicacion.getClass().getSimpleName());
        };
    }

    @Override
    public SantuarioJPADTO toJpa(Santuario santuario) {
        if (santuario == null) {
            return null;
        }
        SantuarioJPADTO santuarioJPA = new SantuarioJPADTO(santuario.getNombre(), santuario.getFlujoDeEnergia());
        santuarioJPA.setId(santuario.getId());
        santuarioJPA.setTipo(santuario.getTipo());
        santuarioJPA.setDeleted(santuario.isDeleted());
        santuarioJPA.setCreatedAt(santuario.getCreatedAt());
        santuarioJPA.setUpdatedAt(santuario.getUpdatedAt());
        return santuarioJPA;
    }

    @Override
    public CementerioJPADTO toJpa(Cementerio cementerio) {
        if (cementerio == null) {
            return null;
        }
        CementerioJPADTO cementerioJPA = new CementerioJPADTO(cementerio.getNombre(), cementerio.getFlujoDeEnergia());
        cementerioJPA.setId(cementerio.getId());
        cementerioJPA.setTipo(cementerio.getTipo());
        cementerioJPA.setDeleted(cementerio.isDeleted());
        cementerioJPA.setCreatedAt(cementerio.getCreatedAt());
        cementerioJPA.setUpdatedAt(cementerio.getUpdatedAt());
        return cementerioJPA;
    }

    @Override
    public SantuarioJPADTO actualizarJpaCon(SantuarioJPADTO ubiJPA, Santuario ubicacion) {
        if (ubiJPA == null || ubicacion == null) {
            throw new IllegalArgumentException("Los objetos DTO o de dominio no pueden ser nulos para la actualización.");
        }
        ubiJPA.setNombre(ubicacion.getNombre());
        ubiJPA.setFlujoDeEnergia(ubicacion.getFlujoDeEnergia());
        ubiJPA.setDeleted(ubicacion.isDeleted());
        return ubiJPA;
    }

    @Override
    public CementerioJPADTO actualizarJpaCon(CementerioJPADTO ubiJPA, Cementerio ubicacion) {
        if (ubiJPA == null || ubicacion == null) {
            throw new IllegalArgumentException("Los objetos DTO o de dominio no pueden ser nulos para la actualización.");
        }
        ubiJPA.setNombre(ubicacion.getNombre());
        ubiJPA.setFlujoDeEnergia(ubicacion.getFlujoDeEnergia());
        return ubiJPA;
    }

    @Override
    public UbicacionJPADTO actualizarJPA(UbicacionJPADTO ubiJPA, Ubicacion ubicacion) {
        TipoUbicacion tipo = ubiJPA.getTipo();
        return switch (tipo) {
            case CEMENTERIO -> this.actualizarJpaCon((CementerioJPADTO) ubiJPA, (Cementerio) ubicacion);
            case SANTUARIO -> this.actualizarJpaCon((SantuarioJPADTO) ubiJPA, (Santuario) ubicacion);
        };
    }


    @Override
    public UbicacionNeoDTO toNeo(Ubicacion ubicacion) {
        if (ubicacion == null) {
            return null;
        }
        UbicacionNeoDTO neoDto = new UbicacionNeoDTO(ubicacion.getId(), ubicacion.getTipo());
        neoDto.setDeleted(ubicacion.isDeleted());

        return neoDto;
    }

}
