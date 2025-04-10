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



    @BeforeEach
    void setUp() {
        ubicacionDao = new HibernateUbicacionDAO();
        serviceU = new UbicacionServiceImpl(ubicacionDao);

        mediumDAO = new HibernateMediumDAO();
        serviceM = new MediumServiceImpl(mediumDAO, espirituDAO);

        espirituDAO = new HibernateEspirituDAO();
        serviceE = new EspirituServiceImpl(espirituDAO, mediumDAO);

        quilmes = new Ubicacion("Quilmes");
        serviceU.crear(quilmes);
        demonio1 = new EspirituDemoniaco( 80, "Azazel", quilmes);
        demonio2 = new EspirituDemoniaco( 100, "Belcebu", quilmes);
        angel = new EspirituAngelical( 90, "Gabriel", quilmes);
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

    @Test
    void testGuardarYRecuperarEspiritu() {
        Espiritu nuevoEspiritu = new EspirituAngelical(50, "Miguel", quilmes);
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

        serviceE.eliminarTodo();

        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertTrue(espiritus.isEmpty());

        // Verificar que el medium fue eliminado
        assertThrows(NoResultException.class, () -> {
            serviceM.recuperar(medium.getId());
        });
    }

    @AfterEach
    void cleanup() {
        serviceE.eliminarTodo();
        serviceM.eliminarTodo();
        serviceU.eliminarTodo();
    }
}