package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.MismaUbicacionException;
import ar.edu.unq.epersgeist.modelo.exception.NombreDeUbicacionRepetidoException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionesNoConectadasException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.ClosenessResult;
import ar.edu.unq.epersgeist.servicios.interfaces.DegreeResult;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionRepository ubicacionRepository;

    public UbicacionServiceImpl(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public Ubicacion guardar(Ubicacion ubicacion) {
        try {
            return ubicacionRepository.guardar(ubicacion);
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
        List<DegreeResult> resultados = new ArrayList<>();
        for (Long id : ids) {
            Ubicacion ubicacion = ubicacionRepository.recuperar(id).orElseThrow(() -> new UbicacionNoEncontradaException(id));
            Double degree = ubicacionRepository.degreeOf(id);
            resultados.add(new DegreeResult(ubicacion, degree));
        }
        return resultados;
    }

    @Override
    public List<Ubicacion> recuperarConexiones(Long ubicacionId) {
        ubicacionRepository.recuperar(ubicacionId)
                .orElseThrow(() -> new UbicacionNoEncontradaException(ubicacionId));

        return ubicacionRepository.recuperarConexiones(ubicacionId);
    }

}