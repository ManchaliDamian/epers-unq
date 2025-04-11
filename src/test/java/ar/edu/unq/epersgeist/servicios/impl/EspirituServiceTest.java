package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateMediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.MediumService;
import ar.edu.unq.epersgeist.servicios.UbicacionService;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class EspirituServiceTest {

    private EspirituService serviceE;
    private EspirituDAO espirituDAO;
    private Espiritu demonio1;
    private Espiritu demonio2;
    private Espiritu angel;

    private MediumService serviceM;
    private MediumDAO mediumDAO;
    private Medium medium;

    private UbicacionService serviceU;
    private Ubicacion quilmes;
    private UbicacionDAO ubicacionDao;

    private GeneradorDeNumeros generadorMock;

    @BeforeEach
    void setUp() {
        generadorMock = mock(GeneradorDeNumeros.class);

        ubicacionDao = new HibernateUbicacionDAO();
        serviceU = new UbicacionServiceImpl(ubicacionDao);

        mediumDAO = new HibernateMediumDAO();
        serviceM = new MediumServiceImpl(mediumDAO, espirituDAO);

        espirituDAO = new HibernateEspirituDAO();
        serviceE = new EspirituServiceImpl(espirituDAO, mediumDAO);

        quilmes = new Ubicacion("Quilmes");
        serviceU.crear(quilmes);

        demonio1 = new EspirituDemoniaco( 80, "Azazel", quilmes, generadorMock);
        demonio2 = new EspirituDemoniaco( 100, "Belcebu", quilmes, generadorMock);
        angel = new EspirituAngelical( 90, "Gabriel", quilmes, generadorMock);
        medium = new Medium("nombre", 150, 30, quilmes);

        serviceE.guardar(demonio1);
        serviceE.guardar(demonio2);

        serviceE.guardar(angel);
    }

    @Test
    void testEspiritusDemoniacos() {

        List<Espiritu> demonios = serviceE.espiritusDemoniacos();

        assertEquals(2, demonios.size());
        assertTrue(demonios.stream().allMatch(e -> e.getTipo() == TipoEspiritu.DEMONIACO));
    }

    @Test
    void testConectarEspirituAMedium() {

        serviceM.crear(medium);

        Medium mediumConectado = serviceE.conectar(demonio1.getId(), medium.getId());

        Espiritu conectado = serviceE.recuperar(demonio1.getId());
        assertEquals(mediumConectado.getId(), conectado.getMediumConectado().getId());

    }

    @AfterEach
    void cleanup() {
        serviceE.eliminarTodo();
        serviceM.eliminarTodo();
        serviceU.eliminarTodo();
    }
}