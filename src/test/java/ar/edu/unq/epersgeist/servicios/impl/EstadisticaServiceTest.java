package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.NotFound.SnapshotNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.Snapshot;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;

import ar.edu.unq.epersgeist.persistencia.DTOs.estadistica.SnapshotMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.PoligonoRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EstadisticaServiceTest {
    @Autowired private EstadisticaService estadisticaService;
    @Autowired private UbicacionService ubicacionService;
    @Autowired private MediumService mediumService;
    @Autowired private EspirituService espirituService;

    @Autowired private UbicacionRepository ubicacionRepository;
    @Autowired private EspirituRepository espirituRepository;
    @Autowired private MediumRepository mediumRepository;
    @Autowired private DataService dataService;
    @Autowired private PoligonoRepository poligonoRepository;

    private Medium medium1;

    private Cementerio cementerio;

    private Santuario santuario1;
    private Santuario santuario2;

    private Espiritu demoniaco1;
    private EspirituDemoniaco demoniaco2;
    private EspirituDemoniaco demoniaco3;
    private Coordenada c1;
    private Coordenada c4;
    private Coordenada c3;
    private Coordenada c2;
    private Coordenada c5;
    private Coordenada c6;
    private Coordenada c7;
    private Coordenada c8;
    private Coordenada c9;
    private Coordenada c10;
    private Coordenada c11;
    private Coordenada c12;
    private Poligono poligono;
    private Poligono poligono1;
    private Poligono poligono2;
    @BeforeEach
    void setUp() {
        c1 = new Coordenada(0.0,0.0);
        c2 = new Coordenada(0.0,1.0);
        c3 = new Coordenada(1.0,1.0);
        c4 = new Coordenada(1.0,0.0);
        List<Coordenada> coordenadas = Arrays.asList(c1, c2, c3, c4, c1);
        poligono = new Poligono(coordenadas);

        c5 = new Coordenada(2.0,2.0);
        c6 = new Coordenada(2.0,3.0);
        c7 = new Coordenada(3.0,3.0);
        c8 = new Coordenada(3.0,2.0);
        List<Coordenada> coordenadas1 = Arrays.asList(c5, c6, c7, c8, c5);
        poligono1 = new Poligono(coordenadas1);

        c9 = new Coordenada(6.0,6.0);
        c10 = new Coordenada(6.0,7.0);
        c11 = new Coordenada(7.0,7.0);
        c12 = new Coordenada(7.0,6.0);
        List<Coordenada> coordenadas2 = Arrays.asList(c9, c10, c11, c12, c9);
        poligono2 = new Poligono(coordenadas2);

        cementerio = new Cementerio("Quilmes",1);

        santuario1 = new Santuario("santuario 1",50);
        santuario2 = new Santuario("santuario 2",50);

        demoniaco1 = new EspirituDemoniaco("demoniaco 1",santuario1);
        demoniaco2 = new EspirituDemoniaco("demoniaco 2",santuario1);
        demoniaco3 = new EspirituDemoniaco("demoniaco 3",santuario2);


        medium1 = new Medium("medium 1",100,50,santuario1);

        ubicacionService.guardar(cementerio, poligono);
        ubicacionService.guardar(santuario1, poligono1);
        ubicacionService.guardar(santuario2, poligono2);

        medium1 = mediumService.guardar(medium1, c5);

        demoniaco1 = espirituService.guardar(demoniaco1, c5);
        espirituService.guardar(demoniaco2, c5);
        espirituService.guardar(demoniaco3, c9);

    }

    @Test
    void elSantuarioMasCorrupto(){

        medium1 = espirituService.conectar(demoniaco1.getId(), medium1.getId());

        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();
        assertEquals(santuario1.getNombre(),reporte.getNombreSantuario());
        assertEquals(2, reporte.getTotalDemonios());
        assertEquals(1,reporte.getCantDemoniosLibres());
    }

    @Test
    void cambiaUbicacionMediumYNoHaySantuarioCorrupto(){
        medium1.setUbicacion(cementerio);

        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();

        assertNull(reporte.getMediumMayorDemoniacos());
    }


    // SNAPSHOT

    @Test
    void crearSnapshotYObtener(){
        LocalDate fechaHoy = LocalDate.now();
        estadisticaService.crearSnapshot();
        Snapshot ssCreadaHoy = estadisticaService.obtenerSnapshot(fechaHoy);
        assertEquals(fechaHoy, ssCreadaHoy.getFecha());
    }

    @Test
    void crearSnapshotPisaElDeLaFechaActual(){
        dataService.eliminarTodo();
        LocalDate fechaHoy = LocalDate.now();

        // se crea un snapshot sin nada persistido
        estadisticaService.crearSnapshot();
        Snapshot snapshotPrimero = estadisticaService.obtenerSnapshot(fechaHoy);

        // se crea segundo snapshot con cosas persistidas
        Poligono poligonoCABA = new Poligono(List.of(
                new Coordenada(1.0, 1.0),
                new Coordenada(1.0, 1.2),
                new Coordenada(1.2, 1.2),
                new Coordenada(1.2, 1.0),
                new Coordenada(1.0, 1.0)
        ));

        Santuario argentina = new Santuario("Argentina",99);
        ubicacionService.guardar(argentina, poligonoCABA);

        Medium unMedium = new Medium("Pepe", 700, 50, argentina);
        mediumService.guardar(unMedium, new Coordenada(1.1, 1.1));

        estadisticaService.crearSnapshot();
        Snapshot snapshotMasReciente = estadisticaService.obtenerSnapshot(fechaHoy);

        //verify
        assertNotEquals(snapshotPrimero.getId(), snapshotMasReciente.getId());
        assertEquals(fechaHoy, snapshotMasReciente.getFecha());
    }

    @Test
    void obtenerSnapshotVacio(){
        dataService.eliminarTodo();
        assertThrows(SnapshotNoEncontradoException.class, () -> estadisticaService.obtenerSnapshot(LocalDate.now()));
    }

    @Test
    void eliminarExitoso(){
        estadisticaService.crearSnapshot();
        Snapshot snapshotRecuperada = estadisticaService.obtenerSnapshot(LocalDate.now());
        estadisticaService.eliminar(snapshotRecuperada);

        assertThrows(SnapshotNoEncontradoException.class, () -> estadisticaService.obtenerSnapshot(LocalDate.now()));
    }

    @AfterEach
    void eliminarTodo(){
        dataService.eliminarTodo();
    }

}
