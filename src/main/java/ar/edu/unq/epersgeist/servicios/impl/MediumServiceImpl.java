package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.BadRequest.CoordenadaFueraDeRangoException;
import ar.edu.unq.epersgeist.exception.Conflict.EspirituMuyLejanoException;
import ar.edu.unq.epersgeist.exception.Conflict.RecursoNoEliminable.MediumNoEliminableException;
import ar.edu.unq.epersgeist.exception.Conflict.UbicacionLejanaException;
import ar.edu.unq.epersgeist.exception.NotFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.exception.NotFound.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.exception.NotFound.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DAOs.MediumDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.PoligonoRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final MediumRepository mediumRepository;
    private final EspirituRepository espirituRepository;
    private final UbicacionRepository ubicacionRepository;
    private final PoligonoRepository poligonoRepository;
    private final MediumDAOMongo mediumDAOMongo;

    public MediumServiceImpl(MediumRepository mediumRepository,
                             EspirituRepository espirituRepository,
                             UbicacionRepository ubicacionRepository,
                             PoligonoRepository poligonoRepository,
                             MediumDAOMongo mediumDAOMongo) {
        this.mediumRepository = mediumRepository;
        this.espirituRepository = espirituRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.poligonoRepository = poligonoRepository;
        this.mediumDAOMongo = mediumDAOMongo;
    }


    @Override
    public Medium guardar(Medium unMedium, Coordenada coordenada) {
        return mediumRepository.guardar(unMedium, coordenada);
    }
    @Override
    public Medium actualizar(Medium medium, Coordenada coordenada) {
        return mediumRepository.actualizar(medium, coordenada);
    }
    @Override
    public Medium actualizar(Medium unMedium) {
        return mediumRepository.actualizar(unMedium);
    }

    private Medium getMedium(Long mediumId) {
        return mediumRepository.recuperar(mediumId)
                .orElseThrow(() -> new MediumNoEncontradoException(mediumId));
    }
    private Ubicacion getUbicacion(Long ubicacionId) {
        return ubicacionRepository.recuperar(ubicacionId)
                .orElseThrow(() -> new UbicacionNoEncontradaException(ubicacionId));
    }

    @Override
    public Optional<Medium> recuperar(Long mediumId) {
        return mediumRepository.recuperar(mediumId)
                .filter(e -> !e.isDeleted());
    }

    @Override
    public void eliminar(Long mediumId) {
        Medium medium = this.getMedium(mediumId);
        if (!medium.getEspiritus().isEmpty()) {
            throw new MediumNoEliminableException(mediumId);
        }
        medium.setDeleted(true);
        mediumRepository.actualizar(medium);
        mediumRepository.eliminarFisicoEnMongoSiExiste(mediumId);
    }

    @Override
    public List<Medium> recuperarTodos() {
        return mediumRepository.recuperarTodos();
    }


    @Override
    public void descansar(Long mediumId) {
        Medium medium = this.getMedium(mediumId);
        medium.descansar();
        mediumRepository.actualizar(medium);
    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
        Medium mediumExorcista = this.getMedium(idMediumExorcista);
        Medium mediumAExorcizar = this.getMedium(idMediumAExorcizar);

        List<EspirituAngelical> angeles = espirituRepository.recuperarAngelesDe(idMediumExorcista);
        List<EspirituDemoniaco> demonios = espirituRepository.recuperarDemoniosDe(idMediumAExorcizar);
        List<EspirituAngelical> angelesCopy = new ArrayList<>(angeles);
        List<EspirituDemoniaco> demoniosCopy = new ArrayList<>(demonios);
        mediumExorcista.exorcizarA(angeles, demonios, mediumAExorcizar.getUbicacion());

        mediumRepository.actualizar(mediumExorcista);
        mediumRepository.actualizar(mediumAExorcizar);
        angelesCopy.forEach(espirituRepository::actualizar);
        demoniosCopy.forEach(espirituRepository::actualizar);
    }

    @Override
    public List<Espiritu> espiritus(Long mediumId) {
        return mediumRepository.findEspiritusByMediumId(mediumId);
    }

    @Override
    public List<EspirituAngelical> angeles(Long mediumId) {
        return espirituRepository.recuperarAngelesDe(mediumId);
    }

    @Override
    public List<EspirituDemoniaco> demonios(Long mediumId) {
        return espirituRepository.recuperarDemoniosDe(mediumId);
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {

        Espiritu espiritu = espirituRepository.recuperar(espirituId)
                .orElseThrow(() -> new EspirituNoEncontradoException(espirituId));

        Medium medium = this.getMedium(mediumId);

        Coordenada coordenada = espirituRepository.recuperarCoordenada(espirituId)
                .orElseThrow(() -> new EspirituNoEncontradoException(espirituId));


        Double distancia = mediumRepository.distanciaA(coordenada.getLatitud(),coordenada.getLongitud(),medium.getId())
                .orElseThrow(() -> new MediumNoEncontradoException(mediumId));

        if (distancia > 50.0){
            throw new EspirituMuyLejanoException(espirituId, mediumId);
        }
        medium.invocarA(espiritu);

        mediumRepository.actualizar(medium);
        espirituRepository.actualizar(espiritu);

        return espiritu;
    }

    @Override
    public void mover(Long mediumId, Double latitud, Double longitud) {
        Medium medium = this.getMedium(mediumId);
        this.validarCoordenada(latitud,longitud);
        Ubicacion origen = medium.getUbicacion();

        Long ubicacionId = poligonoRepository.ubicacionIdConCoordenadas(latitud, longitud)
                .orElseThrow(() -> new UbicacionNoEncontradaException(latitud, longitud));

        Ubicacion destino = ubicacionRepository.recuperar(ubicacionId)
                .orElseThrow(() -> new UbicacionNoEncontradaException(latitud, longitud));

        boolean conectadas = ubicacionRepository.estanConectadas(origen.getId(), destino.getId());
        if (!Objects.equals(origen.getId(), destino.getId()) && !conectadas) {
            throw new UbicacionLejanaException(origen, destino);
        }

        Double distancia = mediumRepository.distanciaA(latitud, longitud, mediumId)
                .orElseThrow(() -> new UbicacionNoEncontradaException(latitud, longitud));

        if (distancia > 30.0){
            throw new UbicacionLejanaException(distancia);
        }

        MediumMongoDTO mediumMongo = mediumDAOMongo.findByIdSQL(medium.getId())
                .orElseThrow(() -> new MediumNoEncontradoException(medium.getId()));

        medium.mover(destino);

        mediumRepository.actualizar(medium);

        Coordenada coordenada = new Coordenada(latitud, longitud);
        GeoJsonPoint punto = new GeoJsonPoint(longitud, latitud);

        mediumMongo.setPunto(punto);
        mediumDAOMongo.save(mediumMongo);

        medium.getEspiritus().forEach(esp -> espirituRepository.actualizar(esp, coordenada));
    }

    private void validarCoordenada(Double latitud, Double longitud){
        if (latitud < -90 || latitud > 90 || longitud < -180 || longitud > 180) {
            throw new CoordenadaFueraDeRangoException(
                    "Coordenada inválida: latitud debe estar entre -90 y +90, "
                            + "longitud entre -180 y +180"
            );
        }
    }
}