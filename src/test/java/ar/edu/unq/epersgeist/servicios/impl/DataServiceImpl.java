package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;

import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.*;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@Transactional
public class DataServiceImpl implements DataService {
    private final UbicacionRepository ubicacionRepository;
    private final MediumRepository mediumRepository;
    private final EspirituRepository espirituRepository;
    private final PoligonoRepository poligonoRepository;
    private final EstadisticaRepository estadisticaRepository;

    public DataServiceImpl(UbicacionRepository ubicacionRepository,
                           MediumRepository mediumRepository,
                           EspirituRepository espirituRepository,
                            PoligonoRepository poligonoRepository,
                            EstadisticaRepository estadisticaRepository
    ) {
        this.ubicacionRepository = ubicacionRepository;
        this.mediumRepository = mediumRepository;
        this.espirituRepository = espirituRepository;
        this.poligonoRepository = poligonoRepository;
        this.estadisticaRepository = estadisticaRepository;
    }

    public void eliminarTodo() {
        poligonoRepository.deleteAll();
        espirituRepository.deleteAll();
        mediumRepository.deleteAll();
        ubicacionRepository.deleteAll();
        estadisticaRepository.deleteAll();
    }

    public Optional<Medium> recuperarEliminadoMedium(Long mediumId) {
        return mediumRepository.recuperarEliminado(mediumId);
    }

    public List<Medium> recuperarTodosMediumsEliminados(){
        return mediumRepository.recuperarTodosLosEliminados();
    }

    public Optional<Espiritu> recuperarEliminadoEspiritu(Long id) {
        return espirituRepository.recuperarEliminado(id);
    }

    public List<Espiritu> recuperarTodosLosEspiritusEliminados() {
        return espirituRepository.recuperarTodosLosEliminados();
    }

    public Optional<Ubicacion> recuperarEliminadoUbicacion(Long id) {
        return ubicacionRepository.recuperarEliminado(id);
    }

    public List<Ubicacion> recuperarTodosEliminadosDeUbicacion() {
        return ubicacionRepository.recuperarTodosEliminados();
    }
    public List<Espiritu> crearYGuardarEspiritusAngelicales(int cantidad, Ubicacion ubi, Coordenada cc) {
        List<Espiritu> espiritusCreados = new ArrayList<>();


        IntStream.range(0, cantidad).forEach(i -> {
            String nombre = "Espiritu_" + i;
            // Ataque y Defensa aleatorios entre 1 y 20
            int ataque = ThreadLocalRandom.current().nextInt(1, 51);
            int defensa = ThreadLocalRandom.current().nextInt(1, 31);

            // Se usa el constructor de Espiritu que inicializa la vida en 100 y setea ataque/defensa.
            Espiritu espiritu = new EspirituAngelical(nombre, ubi, ataque, defensa);

            // Guarda el espíritu usando tu servicio o repositorio.
            // Es crucial que este método devuelva el espíritu con el ID asignado por Firestore.
            Espiritu espirituGuardado = espirituRepository.guardar(espiritu, cc);
            espiritusCreados.add(espirituGuardado);
        });
        return espiritusCreados;
    }

    @Override
    public List<Espiritu> recuperarTodosMayorVida(int vida) {
        return espirituRepository.recuperarTodosMayorVida(vida);
    }

    public List<Espiritu> crearYGuardarEspiritusDemoniacos(int cantidad, Ubicacion ubi, Coordenada cc) {
        List<Espiritu> espiritusCreados = new ArrayList<>();

        IntStream.range(0, cantidad).forEach(i -> {
            String nombre = "Espiritu_" + i;
            // Ataque y Defensa aleatorios entre 1 y 20
            int ataque = ThreadLocalRandom.current().nextInt(1, 31);
            int defensa = ThreadLocalRandom.current().nextInt(1, 31);

            // Se usa el constructor de Espiritu que inicializa la vida en 100 y setea ataque/defensa.
            Espiritu espiritu = new EspirituDemoniaco(nombre, ubi, ataque, defensa);

            // Guarda el espíritu usando tu servicio o repositorio.
            // Es crucial que este método devuelva el espíritu con el ID asignado por Firestore.
            Espiritu espirituGuardado = espirituRepository.guardar(espiritu, cc);
            espiritusCreados.add(espirituGuardado);
        });
        return espiritusCreados;
    }
}

