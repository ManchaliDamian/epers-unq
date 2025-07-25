package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.Conflict.MismaUbicacionException;
import ar.edu.unq.epersgeist.exception.Conflict.NombreDeUbicacionRepetidoException;
import ar.edu.unq.epersgeist.exception.NotFound.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.exception.Conflict.UbicacionesNoConectadasException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.PoligonoRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.ClosenessResult;
import ar.edu.unq.epersgeist.servicios.interfaces.DegreeResult;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final PoligonoRepository poligonoRepository;

    public UbicacionServiceImpl(UbicacionRepository ubicacionRepository, PoligonoRepository poligonoRepository) {
        this.ubicacionRepository = ubicacionRepository;
        this.poligonoRepository = poligonoRepository;
    }

    @Override
    public Ubicacion guardar(Ubicacion ubicacion, Poligono poligono ) {
        try {
            Ubicacion ubicacionGuardada = ubicacionRepository.guardar(ubicacion);

            if (poligono != null) {
                poligonoRepository.guardar(ubicacionGuardada.getId(), poligono);
            }

            return ubicacionGuardada;

        } catch (DataIntegrityViolationException e) {
            throw new NombreDeUbicacionRepetidoException(ubicacion.getNombre());
        }
    }

    @Override
    public Ubicacion actualizar(Ubicacion ubicacion){
        return ubicacionRepository.actualizar(ubicacion);
    }

    @Override
    public Optional<Ubicacion> recuperar(Long ubicacionId) {
        return ubicacionRepository.recuperar(ubicacionId);
    }


    @Override
    public List<Ubicacion> recuperarTodos() {
        return ubicacionRepository.recuperarTodos();
    }


    @Override
    public List<Cementerio> recuperarCementerios() {
        return ubicacionRepository.recuperarCementerios();
    }


    @Override
    public List<Santuario> recuperarSantuarios() {
        return ubicacionRepository.recuperarSantuarios();
    }

    @Override
    public void eliminar(Long id) {
        ubicacionRepository.eliminar(id);
    }

    @Override
    public List<Espiritu> espiritusEn(Long ubicacionId) {
        return ubicacionRepository.findEspiritusByUbicacionId(ubicacionId);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long ubicacionId) {
        return ubicacionRepository.findMediumsSinEspiritusByUbicacionId(ubicacionId);
    }

    @Override
    public boolean estanConectadas(Long idOrigen, Long idDestino){
        return ubicacionRepository.estanConectadas(idOrigen,idDestino);
    }

    @Override
    public  List<Ubicacion> caminoMasCorto(Long idOrigen, Long idDestino){
        Ubicacion origen = ubicacionRepository.recuperar(idOrigen)
                .orElseThrow(() -> new UbicacionNoEncontradaException(idOrigen));

        if (idOrigen.equals(idDestino)) {return List.of(origen);}

        ubicacionRepository.recuperar(idDestino)
                .orElseThrow(() -> new UbicacionNoEncontradaException(idDestino));

        List<Ubicacion> caminoMasCorto = ubicacionRepository.caminoMasCorto(idOrigen, idDestino);

        if (caminoMasCorto.isEmpty()) {throw new UbicacionesNoConectadasException(idOrigen, idDestino);}
        return caminoMasCorto;
    }

    @Override
    public void conectar(Long idOrigen, Long idDestino){
        if(idOrigen.equals(idDestino))
            throw new MismaUbicacionException();

        ubicacionRepository.recuperar(idOrigen)
            .orElseThrow(() -> new UbicacionNoEncontradaException(idOrigen));

        ubicacionRepository.recuperar(idDestino)
            .orElseThrow(() -> new UbicacionNoEncontradaException(idDestino));

        ubicacionRepository.conectar(idOrigen, idDestino);
    }

    @Override
    public List<Ubicacion> ubicacionesSobrecargadas(Integer umbralDeEnergia) {
        return ubicacionRepository.ubicacionesSobrecargadas(umbralDeEnergia);
    }

    @Override
    public List<ClosenessResult> closenessOf(List<Long> ids){
        return ubicacionRepository.closenessOf(ids);
    }

    @Override
    public List<DegreeResult> degreeOf(List<Long> ids){
        return ubicacionRepository.degreeOf(ids);
    }

    @Override
    public List<Ubicacion> recuperarConexiones(Long ubicacionId) {
        ubicacionRepository.recuperar(ubicacionId)
                .orElseThrow(() -> new UbicacionNoEncontradaException(ubicacionId));

        return ubicacionRepository.recuperarConexiones(ubicacionId);
    }

}