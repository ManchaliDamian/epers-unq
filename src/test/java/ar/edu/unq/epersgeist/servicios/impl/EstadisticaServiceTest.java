package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
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

    @Autowired private UbicacionDAO ubicacionDAO;
    @Autowired private EspirituDAO espirituDAO;
    @Autowired private MediumDAO mediumDAO;

    private Medium medium1;
    private Medium medium2;
    private Medium medium3;

    private Cementerio cementerio;

    private Santuario santuario1;
    private Santuario santuario2;


    private EspirituAngelical angelical1;
    private EspirituAngelical angelical2;
    private EspirituAngelical angelical3;

    private EspirituDemoniaco demoniaco1;
    private EspirituDemoniaco demoniaco2;
    private EspirituDemoniaco demoniaco3;

    private DataService serviceEliminarTodo;

    @BeforeEach
     void setUp(){
        serviceEliminarTodo = new DataServiceImpl(ubicacionDAO, mediumDAO, espirituDAO);
        serviceEliminarTodo.eliminarTodo();

            cementerio = new Cementerio("Quilmes",1);

            santuario1 = new Santuario("santuario 1",50);


            angelical1 = new EspirituAngelical("agelical 1", santuario1);

            demoniaco1 = new EspirituDemoniaco("demoniaco 1",cementerio);


            medium1 = new Medium("medium 1",100,50,cementerio);
            medium2 = new Medium("medium 2",100,25,santuario1);

            ubicacionService.guardar(cementerio);
            ubicacionService.guardar(santuario1);

            mediumService.guardar(medium1);
            mediumService.guardar(medium2);

            espirituService.guardar(demoniaco1);

            espirituService.guardar(angelical1);


    }

    @Test
    void elSantuarioMasCorrupto(){
        medium1.conectarseAEspiritu(demoniaco1);
        mediumService.guardar(medium1);
        mediumService.mover(medium1.getId(), santuario1.getId());

        ReporteSantuarioMasCorrupto reporte = estadisticaService.santuarioCorrupto();
        assertEquals(santuario1.getNombre(),reporte.getNombreSantuario());
        assertEquals(1, reporte.getTotalDemonios());
    }

}
