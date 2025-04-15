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
import jakarta.persistence.NoResultException;
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
    private EliminarTodoServiceImpl serviceEliminarTodo;
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
        serviceEliminarTodo = new EliminarTodoServiceImpl(ubicacionDao, mediumDAO, espirituDAO);
    }

    @Test
    void testEspiritusDemoniacosPaginados() {
        List.of(
                // Página 1
                new EspirituDemoniaco(85, "Mephisto", quilmes, generadorMock),
                new EspirituDemoniaco(75, "Lucifer", quilmes, generadorMock),
                // Página 2
                new EspirituDemoniaco(65, "Belial", quilmes, generadorMock),
                new EspirituDemoniaco(55, "Amon", quilmes, generadorMock),
                // Página 3
                new EspirituDemoniaco(45, "Andras", quilmes, generadorMock),
                new EspirituDemoniaco(35, "Vine", quilmes, generadorMock)
        ).forEach(espiritu -> serviceE.guardar(espiritu));

        List<Espiritu> pagina0 = serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 0, 2);
        assertEquals(2, pagina0.size());
        assertEquals("Belcebu", pagina0.get(0).getNombre());
        assertEquals("Mephisto", pagina0.get(1).getNombre());

        List<Espiritu> pagina1 = serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 1, 2);
        assertEquals(2, pagina1.size());
        assertEquals("Azazel", pagina1.get(0).getNombre());
        assertEquals("Lucifer", pagina1.get(1).getNombre());

        List<Espiritu> pagina2 = serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 2, 2);
        assertEquals(2, pagina2.size());
        assertEquals("Belial", pagina2.get(0).getNombre());
        assertEquals("Amon", pagina2.get(1).getNombre());

        //Página inexistente
        List<Espiritu> pagina4 = serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 4, 2);
        assertTrue(pagina4.isEmpty());

        List<Espiritu> pagina0Asc = serviceE.espiritusDemoniacos(Direccion.ASCENDENTE, 0, 2);
        assertEquals(2, pagina0Asc.size());
        assertEquals("Vine", pagina0Asc.get(0).getNombre());
        assertEquals("Andras", pagina0Asc.get(1).getNombre());
    }

    @Test
    void testConectarEspirituAMedium() {

        serviceM.crear(medium);

        Medium mediumConectado = serviceE.conectar(demonio1.getId(), medium.getId());

        Espiritu conectado = serviceE.recuperar(demonio1.getId());
        assertEquals(mediumConectado.getId(), conectado.getMediumConectado().getId());

    }

    @Test
    void testGuardarYRecuperarEspiritu() {
        Espiritu nuevoEspiritu = new EspirituAngelical(50, "Miguel", quilmes, generadorMock);
        serviceE.guardar(nuevoEspiritu);

        Espiritu recuperado = serviceE.recuperar(nuevoEspiritu.getId());
        assertNotNull(recuperado);
        assertEquals("Miguel", recuperado.getNombre());
        assertEquals(50, recuperado.getNivelDeConexion());
    }

    @Test
    void testRecuperarTodos() {
        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertEquals(3, espiritus.size());

        List<Long> idsEsperados = List.of(demonio1.getId(), demonio2.getId(), angel.getId());
        assertTrue(espiritus.stream().allMatch(e -> idsEsperados.contains(e.getId())));
    }

    @Test
    void testActualizar() {
        String nuevoNombre = "Lucifer";
        demonio1.setNombre(nuevoNombre);
        serviceE.actualizar(demonio1);

        Espiritu actualizado = serviceE.recuperar(demonio1.getId());
        assertEquals(nuevoNombre, actualizado.getNombre());
    }

    @Test
    void testEliminar() {
        serviceE.eliminar(angel.getId());

        Espiritu eliminado = serviceE.recuperar(angel.getId());
        assertNull(eliminado);

        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertEquals(2, espiritus.size());
    }

    @Test
    void testEliminarTodo() {
        serviceM.crear(medium);

        serviceEliminarTodo.eliminarTodo();

        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertTrue(espiritus.isEmpty());

        assertThrows(NoResultException.class, () -> {
            serviceM.recuperar(medium.getId());
        });
    }

    @AfterEach
    void cleanup() {
        serviceEliminarTodo.eliminarTodo();
    }
}