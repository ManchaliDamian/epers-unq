package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EstadisticaServiceTest {
    @Autowired private EstadisticaService estadisticaService;
    @Autowired private UbicacionService ubicacionService;
    @Autowired private MediumService mediumService;
    @Autowired private EspirituService espirituService;

    @Autowired private UbicacionDAO ubicacionDAO;
    @Autowired private EspirituDAO espirituDAO;
    @Autowired private MediumDAO mediumDAO;

    private Medium medium1;

    private Cementerio cementerio;

    private Santuario santuario1;
    private Santuario santuario2;


    private EspirituAngelical angelical1;
    private EspirituAngelical angelical2;
    private EspirituAngelical angelical3;

    private EspirituDemoniaco demoniaco1;
    private EspirituDemoniaco demoniaco2;
    private EspirituDemoniaco demoniaco3;

    private DataService dataService;

    @BeforeEach
     void setUp(){
        dataService = new DataServiceImpl(ubicacionDAO, mediumDAO, espirituDAO);

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

            mediumService.guardar(medium1);

            espirituService.guardar(demoniaco1);
            espirituService.guardar(demoniaco2);
            espirituService.guardar(demoniaco3);

            espirituService.guardar(angelical1);
            espirituService.guardar(angelical2);
            espirituService.guardar(angelical3);


    }

    @Test
    void elSantuarioMasCorrupto(){
        medium1.conectarseAEspiritu(demoniaco1);
        mediumService.guardar(medium1);
        mediumService.mover(medium1.getId(), santuario1.getId());

        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();
        assertEquals(santuario1.getNombre(),reporte.getNombreSantuario());
        assertEquals(2, reporte.getTotalDemonios());
        assertEquals(2,reporte.getDemoniosLibres());
    }

    @Test
    @Transactional
    void elMediumQueTieneMasManaLuegoDeEmpatarEnCantDeDemonios(){

        Santuario s1 = new Santuario("Santuario 1", 50);

        Medium medium2 = new Medium("medium 2",10,10,s1);
        Medium medium3 = new Medium("medium 3",60,30,s1);

        EspirituDemoniaco d1 = new EspirituDemoniaco("Demonio 1",s1);
        EspirituDemoniaco d2 = new EspirituDemoniaco("Demonio 2",s1);


        ubicacionService.guardar(s1);

        espirituService.guardar(d1);
        espirituService.guardar(d2);

        medium2.conectarseAEspiritu(d1);
        medium3.conectarseAEspiritu(d2);

        mediumService.guardar(medium2);
        mediumService.guardar(medium3);


        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();
        assertEquals(reporte.getMediumMayorDemoniacos().getNombre(), medium3.getNombre());

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
