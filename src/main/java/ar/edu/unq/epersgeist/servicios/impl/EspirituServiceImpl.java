package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEliminableException;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.enums.Direccion;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.exception.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final EspirituRepository espirituRepository;
    private final MediumRepository mediumRepository;

    public EspirituServiceImpl(EspirituRepository espirituRepository, MediumRepository mediumRepository) {
        this.espirituRepository = espirituRepository;
        this.mediumRepository = mediumRepository;
    }

    @Override
    public Espiritu guardar(Espiritu espiritu) {
        return espirituRepository.save(espiritu);
    }

    @Override
    public Espiritu actualizar(Espiritu espiritu){
        return espirituRepository.save(espiritu);
    }

    private Espiritu getEspiritu(Long espirituId) {
        Espiritu espiritu = espirituRepository.findById(espirituId).orElseThrow(() -> new EspirituNoEncontradoException(espirituId));
        if(espiritu.isDeleted()) {
            throw new EspirituNoEncontradoException(espirituId);
        }
        return espiritu;
    }

    private Medium getMedium(Long mediumId) {
        Medium medium = mediumRepository.findById(mediumId).orElseThrow(() -> new MediumNoEncontradoException(mediumId));
        if(medium.isDeleted()) {
            throw new EspirituNoEncontradoException(mediumId);
        }
        return medium;
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return espirituRepository.findById(espirituId)
                .filter(e -> !e.isDeleted());
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return espirituRepository.recuperarTodos();
    }

    @Override
    public List<EspirituAngelical> recuperarAngeles() {
        return espirituRepository.recuperarAngeles();
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemonios() {
        return espirituRepository.recuperarDemonios();
    }

    @Override
    public void eliminar(Long espirituId) {
        Espiritu espiritu = this.getEspiritu(espirituId);
        if (espiritu.getMediumConectado() != null) {
            throw new EspirituNoEliminableException(espirituId);
        }
        espiritu.setDeleted(true);
        espirituRepository.save(espiritu);
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        Espiritu espiritu = this.getEspiritu(espirituId);

        Medium medium = this.getMedium(mediumId);

        medium.conectarseAEspiritu(espiritu);

        espirituRepository.save(espiritu);
        mediumRepository.save(medium);

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

        return espirituRepository.recuperarDemoniacosPaginados(pageable);
    }

}