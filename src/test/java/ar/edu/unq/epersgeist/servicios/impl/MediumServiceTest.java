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

    private GeneradorDeNumeros generadorMock;
    private EliminarTodoServiceImpl eliminarTodo;
    @BeforeEach
    void setUp() {
        ubicacionDAO = new HibernateUbicacionDAO();
        mediumDAO = new HibernateMediumDAO();
        espirituDAO = new HibernateEspirituDAO();
        generadorMock = mock(GeneradorDeNumeros.class);
        serviceU = new UbicacionServiceImpl(ubicacionDAO);
        serviceM = new MediumServiceImpl(mediumDAO, espirituDAO);
        serviceE = new EspirituServiceImpl(espirituDAO, mediumDAO);

        plata = new Ubicacion("La Plata");
        ubicacion = new Ubicacion("Quilmes");
        serviceU.crear(ubicacion);
        serviceU.crear(plata);

        medium1 = new Medium("Pablo", 100, 50, plata);
        medium2 = new Medium("Fidol", 100, 50, ubicacion);
        espiritu = new EspirituDemoniaco(80, "Jose", ubicacion, generadorMock);
        angel = new EspirituAngelical(100, "kici", plata, generadorMock);
        serviceM.crear(medium1);
        serviceM.crear(medium2);
        serviceE.guardar(espiritu);
        serviceE.guardar(angel);
        eliminarTodo = new EliminarTodoServiceImpl(ubicacionDAO, mediumDAO, espirituDAO);
    }

    @Test
    void testInvocar() {
        Espiritu invocado = serviceM.invocar(medium1.getId(), espiritu.getId());
        assertEquals("La Plata", invocado.getUbicacion().getNombre());
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
        serviceM.eliminarTodo();
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

//    @Test
//    void descansar(){
//        MediumDAO mediumDAOMock = mock(MediumDAO.class);
//        MediumService serviceMMock = new MediumServiceImpl(mediumDAOMock, espirituDAO);
//        EspirituAngelical ang3 = mock(EspirituAngelical.class);
//        when(ang3.estaConectado()).thenReturn(false);
//        when(ang3.getUbicacion()).thenReturn(ubicacion);
//        when(mediumDAOMock.recuperar(medium1.getId())).thenReturn(medium1);
//        medium1.conectarseAEspiritu(ang3);
//
//        serviceMMock.descansar(medium1.getId());
//
//        Medium m1 = serviceMMock.recuperar(medium1.getId());
//        assertEquals(65,m1.getMana());
//        verify(ang3, times(1)).descansar();
//    }
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
