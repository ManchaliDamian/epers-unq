package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.BadRequest.ConectarException;
import ar.edu.unq.epersgeist.exception.BadRequest.CoordenadaFueraDeAreaException;
import ar.edu.unq.epersgeist.exception.Conflict.DistanciaNoCercanaException;
import ar.edu.unq.epersgeist.exception.Conflict.EspirituDominadoException;
import ar.edu.unq.epersgeist.exception.Conflict.EspirituNoDominableException;
import ar.edu.unq.epersgeist.exception.Conflict.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.exception.Conflict.RecursoNoEliminable.EspirituNoEliminableException;
import ar.edu.unq.epersgeist.exception.NotFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.modelo.enums.Direccion;

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
    private Coordenada c5;
    private Coordenada c6;
    private Coordenada c7;
    private Coordenada c8;
    private Coordenada distanciaMas2Km;
    private Poligono poligono;
    private Poligono poligono1;

    @BeforeEach
    void setUp() {
        c1 = new Coordenada(0.0,0.0);
        c2 = new Coordenada(0.0,1.0);
        c3 = new Coordenada(1.0,1.0);
        c4 = new Coordenada(1.0,0.0);
        List<Coordenada> coordenadas = Arrays.asList(c1, c2, c3, c4, c1);
        poligono = new Poligono(coordenadas);
        distanciaMas2Km = new Coordenada(0.0, 0.0314);


        c5 = new Coordenada(2.0,2.0);
        c6 = new Coordenada(2.0,3.0);
        c7 = new Coordenada(3.0,3.0);
        c8 = new Coordenada(3.0,2.0);
        List<Coordenada> coordenadas1 = Arrays.asList(c5, c6, c7, c8, c5);
        poligono1 = new Poligono(coordenadas1);

        quilmes = new Santuario("Quilmes", 100);
        berazategui = new Cementerio("Berazategui",100);

        quilmes = serviceU.guardar(quilmes, poligono);
        berazategui = serviceU.guardar(berazategui, poligono1);

        azazel = new EspirituDemoniaco( "Azazel", quilmes);
        belcebu = new EspirituDemoniaco(  "Belcebu", quilmes);
        angel = new EspirituAngelical( "Gabriel", quilmes);
        medium = new Medium("nombre", 150, 30, quilmes);

        azazel = serviceE.guardar(azazel, c1);
        belcebu = serviceE.guardar(belcebu, distanciaMas2Km);
        angel = serviceE.guardar(angel, c1);

    }

    @Test
    void dominarAUnEspirituDebil() {
        belcebu.setNivelDeConexion(40);
        serviceE.actualizar(belcebu);
        serviceE.dominar(azazel.getId(), belcebu.getId());

        Optional<Espiritu> dominador = serviceE.recuperar(belcebu.getId());
                assertEquals(azazel, dominador.get().getDominador());
    }
    @Test
    void dominarAUnEspirituFuerteNoLoPuedeDominar() {
        belcebu.setNivelDeConexion(70);
        serviceE.actualizar(belcebu);
        serviceE.dominar(azazel.getId(), belcebu.getId());
        Optional<Espiritu> actualizado = serviceE.recuperar(belcebu.getId());
        assertEquals(null, actualizado.get().getDominador());
    }
    @Test
    void espirituDominadoQuiereDominarASuDominadorLanzaException() {
        belcebu.setNivelDeConexion(40);
        serviceE.actualizar(belcebu);
        serviceE.dominar(azazel.getId(), belcebu.getId());
        Optional<Espiritu> actualizado = serviceE.recuperar(belcebu.getId());
        Optional<Espiritu> azazel1 = serviceE.recuperar(azazel.getId());

        assertThrows(EspirituNoDominableException.class, () ->
                serviceE.dominar(actualizado.get().getId(), azazel1.get().getId()) );
    }
    @Test
    void dominarAAlguienFueraDeRangoLanzaExcepcion() {

        angel.setNivelDeConexion(40);
        serviceE.actualizar(angel);

        assertThrows(DistanciaNoCercanaException.class, () -> serviceE.dominar(azazel.getId(), angel.getId()) );
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
    void testConectarEspirituNoDominadoAMediumSaleBien() {
        medium = serviceM.guardar(medium, c1);
        Medium mediumConectado = serviceE.conectar(belcebu.getId(), medium.getId());

        Optional<Espiritu> conectado = serviceE.recuperar(belcebu.getId());

        assertEquals(mediumConectado.getId(), conectado.get().getMediumConectado().getId());

    }

    @Test
    void testConectarEspirituAMediumFallaPorqueEsEspirituEliminado() {

        medium = serviceM.guardar(medium, c1);
        serviceE.eliminar(azazel.getId());

        assertThrows(EspirituNoEncontradoException.class, () -> serviceE.conectar(azazel.getId(), medium.getId()));

        Optional<Espiritu> conectado = dataService.recuperarEliminadoEspiritu(azazel.getId());
        assertNull(conectado.get().getMediumConectado());

    }

    @Test
    void testConectarEspirituAMediumFallaPorqueNoEstanEnLaMismaUbicacion() {
        medium = serviceM.guardar(medium, c1);
        azazel.setUbicacion(berazategui);
        azazel = serviceE.actualizar(azazel);

        assertThrows(EspirituNoEstaEnLaMismaUbicacionException.class, () -> {
            serviceE.conectar(azazel.getId(), medium.getId());
        });
    }

    @Test
    void testConectarEspirituAMediumFallaPorqueElEspirituNoEstaLibre() {

        medium = serviceM.guardar(medium, c1);
        serviceE.conectar(azazel.getId(), medium.getId());

        assertThrows(ConectarException.class, () -> {
            serviceE.conectar(azazel.getId(), medium.getId());
        });
    }

    @Test
    void testConectarEspirituAMediumFallaPorqueEspirituEstaDominado() {
        azazel.setDominador(angel);
        azazel = serviceE.actualizar(azazel);
        medium = serviceM.guardar(medium, c1);

        assertThrows(EspirituDominadoException.class, () -> {
            serviceE.conectar(azazel.getId(), medium.getId());
        });
    }

    @Test
    void testGuardarYRecuperarEspiritu() {
        Espiritu nuevoEspiritu = new EspirituAngelical("Miguel", quilmes);
        nuevoEspiritu = serviceE.guardar(nuevoEspiritu, c1);

        Optional<Espiritu> recuperado = serviceE.recuperar(nuevoEspiritu.getId());
        assertNotNull(recuperado);
        assertEquals("Miguel", recuperado.get().getNombre());
        assertEquals(0, recuperado.get().getNivelDeConexion());
    }
    @Test
    void testGuardarFallaPorCoordenadaNoValida() {
        Espiritu nuevoEspiritu = new EspirituAngelical("Miguel", quilmes);

        assertThrows(CoordenadaFueraDeAreaException.class, () -> serviceE.guardar(nuevoEspiritu, c5) );
    }

    @Test
    void testRecuperarEspirituQuedaEmptyPorEliminadoLogico() {
        Espiritu nuevoEspiritu = new EspirituAngelical("Miguel", quilmes);
        nuevoEspiritu = serviceE.guardar(nuevoEspiritu, c1);
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
        serviceE.actualizar(azazel);

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
        medium = serviceM.guardar(medium, c1);
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
                serviceE.guardar(espiritu, c1);
            }
            azazel.setNivelDeConexion(90);
            belcebu.setNivelDeConexion(10);
            serviceE.actualizar(azazel);
            serviceE.actualizar(belcebu);

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
