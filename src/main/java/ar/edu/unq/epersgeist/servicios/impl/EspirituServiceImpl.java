package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.mapper.EspirituMapper;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEliminableException;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.enums.Direccion;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.exception.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;
import ar.edu.unq.epersgeist.persistencia.EspirituJPA.EspirituJPA;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;
    @Autowired
    private EspirituMapper mapper;

    public EspirituServiceImpl(EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public Espiritu guardar(Espiritu espiritu) {
        EspirituJPA espirituAGuardar = this.mapper.toJPA(espiritu);
        EspirituJPA guardado = espirituDAO.save(espirituAGuardar);
        return this.mapper.toModel(guardado);
    }

    @Override
    public Espiritu actualizar(Espiritu espiritu){
        EspirituJPA espirituAGuardar = this.mapper.toJPA(espiritu);
        EspirituJPA guardado = espirituDAO.save(espirituAGuardar);
        return this.mapper.toModel(guardado);
    }

    private Espiritu getEspiritu(Long espirituId) {
       EspirituJPA espiritu = espirituDAO.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(espirituId));

        if(espiritu.isDeleted()) {
            throw new EspirituNoEncontradoException(espirituId);
        }
        return this.mapper.toModel(espiritu);
    }

    private Medium getMedium(Long mediumId) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(() -> new MediumNoEncontradoException(mediumId));
        if(medium.isDeleted()) {
            throw new EspirituNoEncontradoException(mediumId);
        }
        return medium;
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        Optional<EspirituJPA> recuperado = espirituDAO.findById(espirituId)
                .filter(e -> !e.isDeleted());
        return this.mapper.toModel(recuperado);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return this.mapper.toModelList(espirituDAO.recuperarTodos());
    }

    @Override
    public List<EspirituAngelical> recuperarAngeles() {
        return this.mapper.toModelListAngeles(espirituDAO.recuperarAngeles());
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemonios() {
        return this.mapper.toModelListDemoniaco(espirituDAO.recuperarDemonios());
    }

    @Override
    public void eliminar(Long espirituId) {
        Espiritu espiritu = this.getEspiritu(espirituId);
        if (espiritu.getMediumConectado() != null) {
            throw new EspirituNoEliminableException(espirituId);
        }
        espiritu.setDeleted(true);
        EspirituJPA eliminado = this.mapper.toJPA(espiritu);
        espirituDAO.save(eliminado);
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        Espiritu espiritu = this.getEspiritu(espirituId);

        Medium medium = this.getMedium(mediumId);

        medium.conectarseAEspiritu(espiritu);
        EspirituJPA aGuardar = this.mapper.toJPA(espiritu);
        espirituDAO.save(aGuardar);
        mediumDAO.save(medium);

        return medium;
    }

    @Override
    public List<Espiritu> espiritusDemoniacos(Direccion dir, int pagina, int cantidadPorPagina){
        if (pagina < 1) {
            throw new IllegalArgumentException("El número de página " + pagina + " es menor a 1");
        }
        if (cantidadPorPagina < 0) {
            throw new IllegalArgumentException("La cantidad por pagina " + cantidadPorPagina + " es menor a 0");
        }

        Sort.Direction sortDirection = dir == Direccion.ASCENDENTE ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina-1, cantidadPorPagina, Sort.by(sortDirection, "nivelDeConexion"));

        return this.mapper.toModelList(espirituDAO.recuperarDemoniacosPaginados(pageable));
    }

}