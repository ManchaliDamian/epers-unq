package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.ConectarException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EspirituServiceTest {

    @Autowired private EspirituService serviceE;
    @Autowired private UbicacionService serviceU;
    @Autowired private MediumService serviceM;

    @Autowired private EspirituDAO espirituDAO;
    @Autowired private MediumDAO mediumDAO;
    @Autowired private UbicacionDAO ubicacionDao;

    private Espiritu azazel;
    private Espiritu belcebu;
    private Espiritu angel;

    private Medium medium;

    private Ubicacion quilmes;
    private Ubicacion berazategui;


    private DataService serviceEliminarTodo;
    @BeforeEach
    void setUp() {
        serviceEliminarTodo = new DataServiceImpl(ubicacionDao, mediumDAO, espirituDAO);
        serviceEliminarTodo.eliminarTodo();

        quilmes = new Santuario("Quilmes", 100);
        berazategui = new Cementerio("Berazategui",100);

        serviceU.guardar(quilmes);
        serviceU.guardar(berazategui);

        azazel = new EspirituDemoniaco( "Azazel", quilmes);
        belcebu = new EspirituDemoniaco(  "Belcebu", quilmes);
        angel = new EspirituAngelical( "Gabriel", quilmes);
        medium = new Medium("nombre", 150, 30, quilmes);

        serviceE.guardar(azazel);
        serviceE.guardar(belcebu);

        serviceE.guardar(angel);
    }


    @Test
    void testConectarEspirituAMediumSaleBien() {

        serviceM.guardar(medium);

        Medium mediumConectado = serviceE.conectar(azazel.getId(), medium.getId());

        Optional<Espiritu> conectado = serviceE.recuperar(azazel.getId());
        assertEquals(mediumConectado.getId(), conectado.get().getMediumConectado().getId());

    }
    @Test
    void testConectarEspirituAMediumFallaPorqueNoEstanEnLaMismaUbicacion() {
        serviceM.guardar(medium);
        azazel.setUbicacion(berazategui);
        serviceE.guardar(azazel);

        assertThrows(EspirituNoEstaEnLaMismaUbicacionException.class, () -> {
            serviceE.conectar(azazel.getId(), medium.getId());
        });
    }
    @Test
    void testConectarEspirituAMediumFallaPorqueElEspirituNoEstaLibre() {

        serviceM.guardar(medium);
        serviceE.conectar(azazel.getId(), medium.getId());

        assertThrows(ConectarException.class, () -> {
            serviceE.conectar(azazel.getId(), medium.getId());
        });
    }
    @Test
    void testGuardarYRecuperarEspiritu() {
        Espiritu nuevoEspiritu = new EspirituAngelical("Miguel", quilmes);
        serviceE.guardar(nuevoEspiritu);

        Optional<Espiritu> recuperado = serviceE.recuperar(nuevoEspiritu.getId());
        assertNotNull(recuperado);
        assertEquals("Miguel", recuperado.get().getNombre());
        assertEquals(0, recuperado.get().getNivelDeConexion());
    }

    @Test
    void testRecuperarTodos() {
        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertEquals(3, espiritus.size());

        List<Long> idsEsperados = List.of(azazel.getId(), belcebu.getId(), angel.getId());
        assertTrue(espiritus.stream().allMatch(e -> idsEsperados.contains(e.getId())));
    }

    @Test
    void testActualizar() {
        String nuevoNombre = "Lucifer";
        azazel.setNombre(nuevoNombre);
        serviceE.guardar(azazel);

        Optional<Espiritu> actualizado = serviceE.recuperar(azazel.getId());
        assertEquals(nuevoNombre, actualizado.get().getNombre());
    }

    @Test
    void testEliminar() {
        serviceE.eliminar(angel.getId());

        Optional<Espiritu> eliminado = serviceE.recuperar(angel.getId());
        assertTrue(eliminado.isEmpty());

        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertEquals(2, espiritus.size());
    }

    @Test
    void testEliminarTodo() {
        serviceM.guardar(medium);

        serviceEliminarTodo.eliminarTodo();

        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertTrue(espiritus.isEmpty());
    }

    @Test
    void testRecuperarTodosCuandoNoExisten() {
        serviceEliminarTodo.eliminarTodo();
        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertTrue(espiritus.isEmpty());
    }

    @Nested
    class PaginacionEspiritusDemoniacosTest {
        @BeforeEach
        void setUpPaginacion() {

            List<EspirituDemoniaco> nuevos = List.of(
                    new EspirituDemoniaco("Mephisto", quilmes),
                    new EspirituDemoniaco("Lucifer", quilmes),
                    new EspirituDemoniaco("Belial", quilmes),
                    new EspirituDemoniaco("Amon", quilmes),
                    new EspirituDemoniaco("Andras", quilmes),
                    new EspirituDemoniaco("Vine", quilmes)
            );

            List<Integer> niveles = List.of(80, 75, 60, 55, 25, 15);

            for (int i = 0; i < nuevos.size(); i++) {
                EspirituDemoniaco espiritu = nuevos.get(i);
                espiritu.setNivelDeConexion(niveles.get(i));
                serviceE.guardar(espiritu);
            }
            azazel.setNivelDeConexion(90);
            belcebu.setNivelDeConexion(10);
            serviceE.guardar(azazel);
            serviceE.guardar(belcebu);

        }

        @Test
        void primeraPaginaDescendente_devuelveDosEspiritusOrdenados() {
            List<Espiritu> resultado = serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 1, 2);

            assertEquals(2, resultado.size());
            assertEquals("Azazel", resultado.get(0).getNombre());
            assertEquals("Mephisto", resultado.get(1).getNombre());
        }

        @Test
        void segundaPaginaDescendente_devuelveSiguientesDosEspiritus() {
            List<Espiritu> resultado = serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 2, 2);

            assertEquals(2, resultado.size());
            assertEquals("Lucifer", resultado.get(0).getNombre());
            assertEquals("Belial", resultado.get(1).getNombre());
        }

        @Test
        void paginaInexistente_devuelveListaVacia() {
            List<Espiritu> resultado = serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 11, 2);

            assertTrue(resultado.isEmpty());
        }

        @Test
        void primeraPaginaAscendente_devuelveEspiritusEnOrdenInverso() {
            List<Espiritu> resultado = serviceE.espiritusDemoniacos(Direccion.ASCENDENTE, 1, 2);
            resultado.forEach(e -> System.out.println(e.getNombre() + " - Nivel: " + e.getNivelDeConexion()));

            assertEquals(2, resultado.size());
            assertEquals("Belcebu", resultado.get(0).getNombre());
            assertEquals("Vine", resultado.get(1).getNombre());
        }

        @Test
        void paginaNegativaOCeroLanzaError(){
            IllegalArgumentException exceptionCero = assertThrows(
                    IllegalArgumentException.class,
                    () -> serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 0, 5)
            );

            assertEquals("El número de página 0 es menor a 1", exceptionCero.getMessage());

            IllegalArgumentException exceptionUno = assertThrows(IllegalArgumentException.class, () -> {
                serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, -4, 5);
            });

            assertEquals("El número de página -4 es menor a 1", exceptionUno.getMessage());
        }

        @Test
        void cantidadPorPaginaNegativaLanzaError(){
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 4, -1)
            );

            assertEquals("La cantidad por pagina -1 es menor a 0", exception.getMessage());
        }

    }

    @AfterEach
    void cleanup() {
        serviceEliminarTodo.eliminarTodo();
    }
}
