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

public class MediumServiceTest {

    private MediumService serviceM;
    private EspirituService serviceE;
    private UbicacionService serviceU;

    private MediumDAO mediumDAO;
    private EspirituDAO espirituDAO;
    private UbicacionDAO ubicacionDAO;

    private Medium medium1;
    private Medium medium2;
    private Espiritu demonio;
    private Espiritu angel;
    private Ubicacion quilmes;
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
        Generador.setEstrategia(new GeneradorSecuencial(50));

        plata = new Ubicacion("La Plata");
        quilmes = new Ubicacion("Quilmes");
        serviceU.crear(quilmes);
        serviceU.crear(plata);

        medium1 = new Medium("Pablo", 100, 50, plata);
        medium2 = new Medium("Fidol", 100, 50, quilmes);
        demonio = new EspirituDemoniaco("Jose", quilmes);
        angel = new EspirituAngelical( "kici", plata);
        serviceM.crear(medium1);
        serviceM.crear(medium2);
        serviceE.guardar(demonio);
        serviceE.guardar(angel);
        eliminarTodo = new EliminarTodoServiceImpl(ubicacionDAO, mediumDAO, espirituDAO);
    }

    @Test
    void testInvocar() {
        Espiritu invocado = serviceM.invocar(medium1.getId(), demonio.getId());
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

    @Test
    void testExorcizar(){
        //SETUP
        Generador.setEstrategia(new GeneradorSecuencial(
                5, // ataca angel con 5+85 = 90
                24, // defiende demonio con 24, se desconecta
                7, // ataca rafael con 7 + 90 = 97
                18 // defiende azazel con 18, se desconecta
                ));
        Espiritu azazel = new EspirituDemoniaco("azazel", quilmes);
        Espiritu rafael = new EspirituAngelical( "rafael", plata);
        serviceE.guardar(azazel);
        serviceE.guardar(rafael);

        Medium mediumExorcista  = serviceE.conectar(angel.getId(),serviceE.conectar(rafael.getId(),medium1.getId()).getId());
        Medium mediumExorcizado = serviceE.conectar(demonio.getId(),serviceE.conectar(azazel.getId(),medium2.getId()).getId());
        angel.setNivelDeConexion(85); serviceE.actualizar(angel);
        rafael.setNivelDeConexion(90); serviceE.actualizar(rafael);
        demonio.setNivelDeConexion(30); serviceE.actualizar(demonio);
        azazel.setNivelDeConexion(20); serviceE.actualizar(azazel);

        serviceM.exorcizar(mediumExorcista.getId(), mediumExorcizado.getId());

        mediumExorcista  = serviceM.recuperar(medium1.getId());
        mediumExorcizado = serviceM.recuperar(medium2.getId());
        angel = serviceE.recuperar(angel.getId());
        demonio = serviceE.recuperar(demonio.getId());
        azazel = serviceE.recuperar(azazel.getId());
        rafael = serviceE.recuperar(rafael.getId());


        //VERIFY

        assertEquals(85, angel.getNivelDeConexion());
        assertEquals(30, demonio.getNivelDeConexion());
        assertEquals(90, rafael.getNivelDeConexion());
        assertEquals(20, azazel.getNivelDeConexion());

        //assertTrue(mediumExorcizado.getEspiritus().isEmpty());

    }



    @AfterEach
    void cleanUp() {
        eliminarTodo.eliminarTodo();
    }
}
