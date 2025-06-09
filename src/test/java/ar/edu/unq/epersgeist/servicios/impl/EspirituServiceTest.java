package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.EspirituNoEliminableException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.modelo.enums.Direccion;
import ar.edu.unq.epersgeist.exception.ConectarException;
import ar.edu.unq.epersgeist.exception.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.exception.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EspirituServiceTest {

    @Autowired private EspirituService serviceE;
    @Autowired private UbicacionService serviceU;
    @Autowired private MediumService serviceM;

    @Autowired private EspirituRepository espirituRepository;
    @Autowired private MediumRepository mediumRepository;
    @Autowired private UbicacionRepository ubicacionRepository;
    @Autowired private DataService dataService;

    private Espiritu azazel;
    private Espiritu belcebu;
    private Espiritu angel;

    private Medium medium;

    private Ubicacion quilmes;
    private Ubicacion berazategui;

    private Coordenada c1;
    private Coordenada c4;
    private Coordenada c3;
    private Coordenada c2;
    private Poligono poligono;

    @BeforeEach
    void setUp() {
        c1 = new Coordenada(1.0,1.0);
        c2 = new Coordenada(2.0,2.0);
        c3 = new Coordenada(3.0,3.0);
        c4 = new Coordenada(-1.0,-1.0);
        List<Coordenada> coordenadas = Arrays.asList(c1, c2, c3, c4, c1);
        poligono = new Poligono(coordenadas);
        quilmes = new Santuario("Quilmes", 100);
        berazategui = new Cementerio("Berazategui",100);

        quilmes = serviceU.guardar(quilmes, poligono);
        berazategui = serviceU.guardar(berazategui, poligono);

        azazel = new EspirituDemoniaco( "Azazel", quilmes, c1);
        belcebu = new EspirituDemoniaco(  "Belcebu", quilmes, c1);
        angel = new EspirituAngelical( "Gabriel", quilmes, c1);
        medium = new Medium("nombre", 150, 30, quilmes, c1);

        azazel = serviceE.guardar(azazel);
        belcebu = serviceE.guardar(belcebu);
        angel = serviceE.guardar(angel);

    }

    @Test
    void testUpdateATDeEspiritu(){
        String nuevoNombre = "Nuevo Azazel";

        azazel.setNombre(nuevoNombre);
        azazel.setUbicacion(berazategui);
        serviceE.actualizar(azazel);

        Date fechaEsperada = new Date();

        Date fechaEspiritu = azazel.getUpdatedAt();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String esperadaFormateada = sdf.format(fechaEsperada);
        String obtenidaFormateada = sdf.format(fechaEspiritu);

        assertEquals(esperadaFormateada, obtenidaFormateada);
        assertEquals(azazel.getNombre(),nuevoNombre);
        assertEquals(azazel.getUbicacion().getNombre(),berazategui.getNombre());

    }

    @Test
    void testCreateAtDeEspiritu(){

        Date fechaEsperada = new Date();

        Date fechaEspiritu = azazel.getCreatedAt();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String esperadaFormateada = sdf.format(fechaEsperada);
        String obtenidaFormateada = sdf.format(fechaEspiritu);

        assertEquals(esperadaFormateada, obtenidaFormateada);

    }

    @Test
    void testConectarEspirituAMediumSaleBien() {
        medium = serviceM.guardar(medium);
        Medium mediumConectado = serviceE.conectar(azazel.getId(), medium.getId());

        Optional<Espiritu> conectado = serviceE.recuperar(azazel.getId());

        assertEquals(mediumConectado.getId(), conectado.get().getMediumConectado().getId());

    }
    @Test
    void testConectarEspirituAMediumFallaPorqueEsEspirituEliminado() {

        medium = serviceM.guardar(medium);
        serviceE.eliminar(azazel.getId());

        assertThrows(EspirituNoEncontradoException.class, () -> serviceE.conectar(azazel.getId(), medium.getId()));

        Optional<Espiritu> conectado = dataService.recuperarEliminadoEspiritu(azazel.getId());
        assertNull(conectado.get().getMediumConectado());


    }
    @Test
    void testConectarEspirituAMediumFallaPorqueNoEstanEnLaMismaUbicacion() {
        medium = serviceM.guardar(medium);
        azazel.setUbicacion(berazategui);
        azazel = serviceE.guardar(azazel);

        assertThrows(EspirituNoEstaEnLaMismaUbicacionException.class, () -> {
            serviceE.conectar(azazel.getId(), medium.getId());
        });
    }
    @Test
    void testConectarEspirituAMediumFallaPorqueElEspirituNoEstaLibre() {

        medium = serviceM.guardar(medium);
        serviceE.conectar(azazel.getId(), medium.getId());

        assertThrows(ConectarException.class, () -> {
            serviceE.conectar(azazel.getId(), medium.getId());
        });
    }
    @Test
    void testGuardarYRecuperarEspiritu() {
        Espiritu nuevoEspiritu = new EspirituAngelical("Miguel", quilmes, c1);
        nuevoEspiritu = serviceE.guardar(nuevoEspiritu);

        Optional<Espiritu> recuperado = serviceE.recuperar(nuevoEspiritu.getId());
        assertNotNull(recuperado);
        assertEquals("Miguel", recuperado.get().getNombre());
        assertEquals(0, recuperado.get().getNivelDeConexion());
    }
    @Test
    void testRecuperarEspirituQuedaEmptyPorEliminadoLogico() {
        Espiritu nuevoEspiritu = new EspirituAngelical("Miguel", quilmes, c1);
        nuevoEspiritu = serviceE.guardar(nuevoEspiritu);
        serviceE.eliminar(nuevoEspiritu.getId());

        assertTrue(serviceE.recuperar(nuevoEspiritu.getId()).isEmpty());
    }

    @Test
    void testRecuperarTodos() {
        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertEquals(3, espiritus.size());
    }
    @Test
    void testRecuperarTodosCuandoHayUnEliminadoLogico() {
        serviceE.eliminar(angel.getId());
        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertEquals(2, espiritus.size());

    }
    @Test
    void testRecuperarTodosCuandoTodosEliminadosLogicamente() {
        serviceE.eliminar(angel.getId());
        serviceE.eliminar(azazel.getId());
        serviceE.eliminar(belcebu.getId());
        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertEquals(0, espiritus.size());
    }
    @Test
    void testRecuperarTodosLosEliminadosLogicamente() {
        serviceE.eliminar(angel.getId());
        serviceE.eliminar(azazel.getId());

        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertEquals(1, espiritus.size());

        List<Espiritu> espiritusEliminados = dataService.recuperarTodosLosEspiritusEliminados();
        assertEquals(2, espiritusEliminados.size());
    }

    @Test
    void testRecuperarAngelesExistentes(){
        List<EspirituAngelical> angeles = serviceE.recuperarAngeles();
        assertEquals(1, angeles.size());
    }

    @Test
    void testRecuperarDemoniosExistentes(){
        List<EspirituDemoniaco> demonios = serviceE.recuperarDemonios();
        assertEquals(2, demonios.size());
    }

    @Test
    void testRecuperarAngelesNoExistentes(){
        serviceE.eliminar(angel.getId());
        List<EspirituAngelical> angeles = serviceE.recuperarAngeles();
        assertEquals(0, angeles.size());
    }

    @Test
    void testRecuperarDemoniosNoExistentes(){
        serviceE.eliminar(belcebu.getId());
        serviceE.eliminar(azazel.getId());
        List<EspirituDemoniaco> demonios = serviceE.recuperarDemonios();
        assertEquals(0, demonios.size());
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

        assertTrue(serviceE.recuperar(angel.getId()).isEmpty());
    }
    @Test
    void eliminarEspirituConMediumConectadoLanzaException() {
        medium = serviceM.guardar(medium);
        serviceE.conectar(angel.getId(), medium.getId());

        assertThrows(EspirituNoEliminableException.class, () -> serviceE.eliminar(angel.getId()));
    }
    @Test
    void testRecuperarEliminadoPorId() {
        serviceE.eliminar(angel.getId());
        Optional<Espiritu> eliminado = dataService.recuperarEliminadoEspiritu(angel.getId());
        assertEquals("Gabriel", eliminado.get().getNombre());
        assertTrue(eliminado.get().isDeleted());

    }

    @Test
    void testEliminarCuandoEstaBorradoLanzaExcepcion(){
        serviceE.eliminar(angel.getId());
        assertThrows(EspirituNoEncontradoException.class, () -> serviceE.eliminar(angel.getId()));
    }

    @Test
    void testEliminarCuandoNoExisteLanzaExcepcion(){
        assertThrows(EspirituNoEncontradoException.class, () -> serviceE.eliminar(412943L));
    }

    @Test
    void testRecuperarTodosCuandoNoExisten() {
        dataService.eliminarTodo();
        List<Espiritu> espiritus = serviceE.recuperarTodos();
        assertTrue(espiritus.isEmpty());
    }

    @Nested
    class PaginacionEspiritusDemoniacosTest {
        @BeforeEach
        void setUpPaginacion() {

            List<EspirituDemoniaco> nuevos = List.of(
                    new EspirituDemoniaco("Mephisto", quilmes, c1),
                    new EspirituDemoniaco("Lucifer", quilmes, c1),
                    new EspirituDemoniaco("Belial", quilmes, c1),
                    new EspirituDemoniaco("Amon", quilmes, c1),
                    new EspirituDemoniaco("Andras", quilmes, c1),
                    new EspirituDemoniaco("Vine", quilmes, c1)
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
        void primeraPaginaDescendente_devuelveEspiritusOrdenadoSinEliminado() {
            serviceE.eliminar(azazel.getId());
            List<Espiritu> resultado = serviceE.espiritusDemoniacos(Direccion.DESCENDENTE, 1, 2);

            assertEquals(2, resultado.size());
            assertEquals("Mephisto", resultado.get(0).getNombre());
            assertEquals("Lucifer", resultado.get(1).getNombre());
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
        dataService.eliminarTodo();
    }
}
