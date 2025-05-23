package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;

import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    private Medium medium1;

    private Cementerio cementerio;

    private Santuario santuario1;
    private Santuario santuario2;


    private EspirituAngelical angelical1;
    private EspirituAngelical angelical2;
    private EspirituAngelical angelical3;

    private Espiritu demoniaco1;
    private EspirituDemoniaco demoniaco2;
    private EspirituDemoniaco demoniaco3;

    private DataService dataService;

    @BeforeEach
     void setUp(){
        dataService = new DataServiceImpl(ubicacionRepository, mediumRepository, espirituRepository);

            cementerio = new Cementerio("Quilmes",1);

            santuario1 = new Santuario("santuario 1",50);
            santuario2 = new Santuario("santuario21",50);


            angelical1 = new EspirituAngelical("agelical 1", santuario1);
            angelical2 = new EspirituAngelical("agelical 1", santuario2);
            angelical3 = new EspirituAngelical("agelical 1", santuario2);

            demoniaco1 = new EspirituDemoniaco("demoniaco 1",santuario1);
            demoniaco2 = new EspirituDemoniaco("demoniaco 2",santuario1);
            demoniaco3 = new EspirituDemoniaco("demoniaco 3",santuario2);


            medium1 = new Medium("medium 1",100,50,santuario1);

            ubicacionService.guardar(cementerio);
            ubicacionService.guardar(santuario1);
            ubicacionService.guardar(santuario2);

            medium1 = mediumService.guardar(medium1);

            demoniaco1 = espirituService.guardar(demoniaco1);
            espirituService.guardar(demoniaco2);
            espirituService.guardar(demoniaco3);

            espirituService.guardar(angelical1);
            espirituService.guardar(angelical2);
            espirituService.guardar(angelical3);


    }

    @Test
    void elSantuarioMasCorrupto(){

        medium1 = espirituService.conectar(demoniaco1.getId(), medium1.getId());
//        medium1.conectarseAEspiritu(demoniaco1);
//        medium1 = mediumService.actualizar(medium1);
        mediumService.mover(medium1.getId(), santuario1.getId());

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
