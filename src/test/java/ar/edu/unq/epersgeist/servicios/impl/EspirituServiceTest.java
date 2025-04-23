package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exception.ConectarException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EspirituServiceTest {

    private EspirituService serviceE;
    private EspirituDAO espirituDAO;
    private Espiritu azazel;
    private Espiritu belcebu;
    private Espiritu angel;

    private MediumService serviceM;
    private MediumDAO mediumDAO;
    private Medium medium;

    private UbicacionService serviceU;
    private Ubicacion quilmes;
    private Ubicacion berazategui;
    private UbicacionDAO ubicacionDao;

    private EliminarTodoServiceImpl serviceEliminarTodo;
    @BeforeEach
    void setUp() {

        serviceU = new UbicacionServiceImpl(ubicacionDao);

        serviceM = new MediumServiceImpl(mediumDAO, espirituDAO);


        serviceE = new EspirituServiceImpl(espirituDAO, mediumDAO);
        //espirituDAO = new EspirituDAO();

        quilmes = new Ubicacion("Quilmes");
        berazategui = new Ubicacion("Berazategui");

        serviceU.crear(quilmes);
        serviceU.crear(berazategui);

        azazel = new EspirituDemoniaco( "Azazel", quilmes);
        belcebu = new EspirituDemoniaco(  "Belcebu", quilmes);
        angel = new EspirituAngelical( "Gabriel", quilmes);
        medium = new Medium("nombre", 150, 30, quilmes);

        serviceE.guardar(azazel);
        serviceE.guardar(belcebu);

        serviceE.guardar(angel);
        serviceEliminarTodo = new EliminarTodoServiceImpl(ubicacionDao, mediumDAO, espirituDAO);
    }


    @Test
    void testConectarEspirituAMediumSaleBien() {

        serviceM.crear(medium);

        Medium mediumConectado = serviceE.conectar(azazel.getId(), medium.getId());

        Espiritu conectado = serviceE.recuperar(azazel.getId());
        assertEquals(mediumConectado.getId(), conectado.getMediumConectado().getId());

    }
//    @Test
//    void testConectarEspirituAMediumFallaPorqueNoEstanEnLaMismaUbicacion() {
//        serviceM.crear(medium);
//        azazel.setUbicacion(berazategui);
//        serviceE.actualizar(azazel);
//
//        assertThrows(EspirituNoEstaEnLaMismaUbicacionException.class, () -> {
//            serviceE.conectar(azazel.getId(), medium.getId());
//        });
//    }
//    @Test
//    void testConectarEspirituAMediumFallaPorqueElEspirituNoEstaLibre() {
//
//        serviceM.crear(medium);
//        serviceE.conectar(azazel.getId(), medium.getId());
//
//        assertThrows(ConectarException.class, () -> {
//            serviceE.conectar(azazel.getId(), medium.getId());
//        });
//    }
//    @Test
//    void testGuardarYRecuperarEspiritu() {
//        Espiritu nuevoEspiritu = new EspirituAngelical("Miguel", quilmes);
//        serviceE.guardar(nuevoEspiritu);
//
//        Espiritu recuperado = serviceE.recuperar(nuevoEspiritu.getId());
//        assertNotNull(recuperado);
//        assertEquals("Miguel", recuperado.getNombre());
//        assertEquals(0, recuperado.getNivelDeConexion());
//    }
//
//    @Test
//    void testRecuperarTodos() {
//        List<Espiritu> espiritus = serviceE.recuperarTodos();
//        assertEquals(3, espiritus.size());
//
//        List<Long> idsEsperados = List.of(azazel.getId(), belcebu.getId(), angel.getId());
//        assertTrue(espiritus.stream().allMatch(e -> idsEsperados.contains(e.getId())));
//    }
//
//    @Test
//    void testActualizar() {
//        String nuevoNombre = "Lucifer";
//        azazel.setNombre(nuevoNombre);
//        serviceE.actualizar(azazel);
//
//        Espiritu actualizado = serviceE.recuperar(azazel.getId());
//        assertEquals(nuevoNombre, actualizado.getNombre());
//    }
//
//    @Test
//    void testEliminar() {
//        serviceE.eliminar(angel.getId());
//
//        Espiritu eliminado = serviceE.recuperar(angel.getId());
//        assertNull(eliminado);
//
//        List<Espiritu> espiritus = serviceE.recuperarTodos();
//        assertEquals(2, espiritus.size());
//    }
//
//    @Test
//    void testEliminarTodo() {
//        serviceM.crear(medium);
//
//        serviceEliminarTodo.eliminarTodo();
//
//        List<Espiritu> espiritus = serviceE.recuperarTodos();
//        assertTrue(espiritus.isEmpty());
//
//        assertThrows(NoResultException.class, () -> {
//            serviceM.recuperar(medium.getId());
//        });
//    }
//
//    @Test
//    void testRecuperarTodosCuandoNoExisten() {
//        serviceEliminarTodo.eliminarTodo();
//        List<Espiritu> espiritus = serviceE.recuperarTodos();
//        assertTrue(espiritus.isEmpty());
//    }
//
//    @Nested
//    class PaginacionEspiritusDemoniacosTest {
//        @BeforeEach
//        void setUpPaginacion() {
//
//            List<EspirituDemoniaco> nuevos = List.of(
//                    new EspirituDemoniaco("Mephisto", quilmes),
//                    new EspirituDemoniaco("Lucifer", quilmes),
//                    new EspirituDemoniaco("Belial", quilmes),
//                    new EspirituDemoniaco("Amon", quilmes),
//                    new EspirituDemoniaco("Andras", quilmes),
//                    new EspirituDemoniaco("Vine", quilmes)
//            );
//
//            List<Integer> niveles = List.of(80, 75, 60, 55, 25, 15);
//
//            for (int i = 0; i < nuevos.size(); i++) {
//                EspirituDemoniaco espiritu = nuevos.get(i);
//                espiritu.setNivelDeConexion(niveles.get(i));
//                serviceE.guardar(espiritu);
//            }
//            azazel.setNivelDeConexion(90);
//            belcebu.setNivelDeConexion(10);
//            serviceE.actualizar(azazel);
//            serviceE.actualizar(belcebu);
//
//        }
//
//        @Test
//        void primeraPaginaDescendente_devuelveDosEspiritusOrdenados() {
//            List<Espiritu> resultado = serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 0, 2);
//
//            assertEquals(2, resultado.size());
//            assertEquals("Azazel", resultado.get(0).getNombre());
//            assertEquals("Mephisto", resultado.get(1).getNombre());
//        }
//
//        @Test
//        void segundaPaginaDescendente_devuelveSiguientesDosEspiritus() {
//            List<Espiritu> resultado = serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 1, 2);
//
//            assertEquals(2, resultado.size());
//            assertEquals("Lucifer", resultado.get(0).getNombre());
//            assertEquals("Belial", resultado.get(1).getNombre());
//        }
//
//        @Test
//        void paginaInexistente_devuelveListaVacia() {
//            List<Espiritu> resultado = serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 10, 2);
//
//            assertTrue(resultado.isEmpty());
//        }
//
//        @Test
//        void primeraPaginaAscendente_devuelveEspiritusEnOrdenInverso() {
//            List<Espiritu> resultado = serviceE.espiritusDemoniacos(Direccion.ASCENDENTE, 0, 2);
//            resultado.forEach(e -> System.out.println(e.getNombre() + " - Nivel: " + e.getNivelDeConexion()));
//
//            assertEquals(2, resultado.size());
//            assertEquals("Belcebu", resultado.get(0).getNombre());
//            assertEquals("Vine", resultado.get(1).getNombre());
//        }
//
//    }
//
    @AfterEach
    void cleanup() {
        serviceEliminarTodo.eliminarTodo();
    }
}
