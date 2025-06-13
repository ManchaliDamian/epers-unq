package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;

import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
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

    @AfterEach
    void eliminarTodo(){
        dataService.eliminarTodo();
    }

}
