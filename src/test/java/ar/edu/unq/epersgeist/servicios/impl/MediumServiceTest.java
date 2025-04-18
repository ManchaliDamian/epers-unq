package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituOcupado;
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
import static org.mockito.Mockito.*;

public class MediumServiceTest {

    private MediumService serviceM;
    private EspirituService serviceE;
    private UbicacionService serviceU;

    private MediumDAO mediumDAO;
    private EspirituDAO espirituDAO;
    private UbicacionDAO ubicacionDAO;

    private Medium medium1;
    private Medium medium2;
    private Espiritu espiritu;
    private Espiritu angel;
    private Ubicacion ubicacion;
    private Ubicacion plata;

    private EliminarTodoServiceImpl eliminarTodo;
    @BeforeEach
    void setUp() {
        ubicacionDAO = new HibernateUbicacionDAO();
        mediumDAO = new HibernateMediumDAO();
        espirituDAO = new HibernateEspirituDAO();
        serviceU = new UbicacionServiceImpl(ubicacionDAO);
        serviceM = new MediumServiceImpl(mediumDAO, espirituDAO);
        serviceE = new EspirituServiceImpl(espirituDAO, mediumDAO);

        plata = new Ubicacion("La Plata");
        ubicacion = new Ubicacion("Quilmes");
        serviceU.crear(ubicacion);
        serviceU.crear(plata);

        medium1 = new Medium("Pablo", 100, 50, plata);
        medium2 = new Medium("Fidol", 100, 50, ubicacion);
        espiritu = new EspirituDemoniaco("Jose", ubicacion);
        angel = new EspirituAngelical( "kici", plata);
        serviceM.crear(medium1);
        serviceM.crear(medium2);
        serviceE.guardar(espiritu);
        serviceE.guardar(angel);
        eliminarTodo = new EliminarTodoServiceImpl(ubicacionDAO, mediumDAO, espirituDAO);
    }

    @Test
    void testInvocarSaleBien() {
        Espiritu invocado = serviceM.invocar(medium1.getId(), espiritu.getId());
        assertEquals("La Plata", invocado.getUbicacion().getNombre());
    }
    @Test
    void testInvocarFallaPorqueEspirituYaEstaConectado() {
        espiritu.setMediumConectado(medium1);
        serviceE.actualizar(espiritu);
        assertThrows(ExceptionEspirituOcupado.class, () -> {
            serviceM.invocar(medium1.getId(), espiritu.getId());
        });
    }
    @Test
    void testInvocarDevuelveAlMismoEspirituPorqueSeTieneSuficienteMana() {
        medium1.setMana(7);
        serviceM.actualizar(medium1);
        Espiritu espirituRecuperado = serviceM.invocar(medium1.getId(), espiritu.getId());
        assertNotEquals(espiritu.getUbicacion(), espirituRecuperado.getUbicacion());
    }
    @Test
    void testCrearYRecuperarMedium() {
        Medium recuperado = serviceM.recuperar(medium1.getId());
        assertEquals(medium1.getNombre(), recuperado.getNombre());
        assertEquals(medium1.getMana(), recuperado.getMana());
    }

    @Test
    void testRecuperarTodosLosMediums() {
        List<Medium> todos = serviceM.recuperarTodos();
        assertEquals(2, todos.size());
    }

    @Test
    void testEliminarMedium() {
        serviceM.eliminar(medium1.getId());
        List<Medium> restantes = serviceM.recuperarTodos();
        assertEquals(1, restantes.size());
    }

    @Test
    void testEliminarTodosLosMediums() {
        eliminarTodo.eliminarTodo();
        List<Medium> vacio = serviceM.recuperarTodos();
        assertTrue(vacio.isEmpty());
    }

    @Test
    void testEspiritusDeUnMedium() {
        medium1.conectarseAEspiritu(angel);
        serviceM.actualizar(medium1);

        List<Espiritu> espiritusDelMedium = serviceM.espiritus(medium1.getId());

        assertEquals(1, espiritusDelMedium.size());
        assertTrue(espiritusDelMedium.stream().anyMatch(e -> e.getId().equals(angel.getId())));
    }

    @Test
    void descansarConEspiritus(){
        angel.setNivelDeConexion(10);
        serviceE.actualizar(angel);
        medium1.setMana(5);
        medium1.conectarseAEspiritu(angel);
        serviceM.actualizar(medium1);
        serviceM.descansar(medium1.getId());
        Medium mediumRecuperado = serviceM.recuperar(medium1.getId());
        Espiritu angelRecuperado = serviceE.recuperar(angel.getId());
        assertEquals(20, mediumRecuperado.getMana());
        assertEquals(15, angelRecuperado.getNivelDeConexion());
    }
    @Test
    void descansarSinEspiritus(){
        medium1.setMana(5);
        medium1.conectarseAEspiritu(angel);
        serviceM.actualizar(medium1);
        serviceM.descansar(medium1.getId());
        Medium mediumRecuperado = serviceM.recuperar(medium1.getId());
        assertEquals(20, mediumRecuperado.getMana());
    }
    @Test
    void descansarPeroElMagoLlegaAlLimiteDeMana(){
        medium1.setMana(98);
        medium1.conectarseAEspiritu(angel);
        serviceM.actualizar(medium1);
        serviceM.descansar(medium1.getId());
        Medium mediumRecuperado = serviceM.recuperar(medium1.getId());
        assertEquals(100, mediumRecuperado.getMana());
    }
//    @Test
//    void testExorcizar(){
//
//
//        when(generadorMock.entre(1, 100)).thenReturn(30); // ejemplo de número aleatorio
//        medium2.conectarseAEspiritu(espiritu);
//        medium1.conectarseAEspiritu(angel);
//        serviceM.actualizar(medium2);
//        serviceE.actualizar(espiritu);
//        serviceM.actualizar(medium1);
//        serviceE.actualizar(angel);
//
//        serviceM.exorcizar(medium1.getId(), medium2.getId());
//        Medium recuperarM = serviceM.recuperar(medium1.getId());
//        assertEquals(20, recuperarM.getEspiritus()
//                    .stream()
//                    .filter(e -> e.getId() == espiritu.getId())
//                    .findFirst());
//    }



    @AfterEach
    void cleanUp() {
        eliminarTodo.eliminarTodo();
    }
}
