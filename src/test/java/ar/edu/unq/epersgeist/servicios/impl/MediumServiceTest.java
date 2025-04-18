package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
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
    void exorcizarA_AtaqueExitoso_DemonioDerrotado() {
        Generador.setEstrategia(new GeneradorSecuencial(10, 1)); // 10 + nivel > 1

        angel.setNivelDeConexion(20);
        demonio.setNivelDeConexion(5);

        conectarEspirituAMedium(medium1, angel); // angel: 20 + 10 = 30 (daño = 15)
        conectarEspirituAMedium(medium2, demonio); // demonio: 5 + 10 = 15

        serviceM.exorcizar(medium1.getId(), medium2.getId());

        Espiritu demonioActualizado = serviceE.recuperar(demonio.getId());
        assertFalse(demonioActualizado.estaConectado());
        assertEquals(0, demonioActualizado.getNivelDeConexion());

        List<Espiritu> espiritusMedium2 = serviceM.espiritus(medium2.getId());
        assertTrue(espiritusMedium2.isEmpty());
    }

    @Test
    void exorcizar_DosAngelesDerrotanUnDemonio() {
        // Configurar
        Generador.setEstrategia(new GeneradorSecuencial(10, 1)); // Ataques exitosos

        EspirituAngelical angel1 = new EspirituAngelical("Ángel1", plata);
        EspirituAngelical angel2 = new EspirituAngelical("Ángel2", plata);
        angel1.setNivelDeConexion(20); // Daño: 10
        angel2.setNivelDeConexion(30); // Daño: 15

        demonio.setNivelDeConexion(25); // 25 + 10 (conexión) = 35

        // Conectar
        conectarEspirituAMedium(medium1, angel1);
        conectarEspirituAMedium(medium1, angel2);
        conectarEspirituAMedium(medium2, demonio);

        // Ejecutar
        serviceM.exorcizar(medium1.getId(), medium2.getId());

        // Verificar
        Espiritu demonioActualizado = serviceE.recuperar(demonio.getId());
        assertEquals(0, demonioActualizado.getNivelDeConexion());
        assertFalse(demonioActualizado.estaConectado());
    }

    @Test
    void exorcizar_ExorcistaSinAngeles_LanzaExcepcion() {
        // No conectar ángeles a medium1
        conectarEspirituAMedium(medium2, demonio);

        assertThrows(ExorcistaSinAngelesException.class, () -> {
            serviceM.exorcizar(medium1.getId(), medium2.getId());
        });
    }

    @Test
    void exorcizar_DemonioYaDesconectado_NoHaceNada() {
        // Configurar demonio desconectado
        demonio.setNivelDeConexion(0);
        demonio.setMediumConectado(null);
        serviceE.actualizar(demonio);

        conectarEspirituAMedium(medium1, angel);

        assertDoesNotThrow(() -> {
            serviceM.exorcizar(medium1.getId(), medium2.getId());
        });
    }

    private void conectarEspirituAMedium(Medium medium, Espiritu espiritu) {
        medium.conectarseAEspiritu(espiritu);
        serviceM.actualizar(medium);
        serviceE.actualizar(espiritu);
    }

    @AfterEach
    void cleanUp() {
        eliminarTodo.eliminarTodo();
    }
}
