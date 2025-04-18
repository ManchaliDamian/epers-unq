package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateMediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UbicacionServiceTest {

    private UbicacionServiceImpl serviceU;
    private Ubicacion quilmes;
    private Ubicacion bernal;
    private UbicacionDAO ubicacionDao;

    private EspirituServiceImpl serviceE;
    private EspirituDAO espirituDAO;

    private MediumServiceImpl serviceM;
    private MediumDAO mediumDAO;
    private Medium medium;
    private Espiritu angel;
    private Espiritu demonio;

    private EliminarTodoServiceImpl serviceEliminarTodo;
    @BeforeEach
    void prepare() {
        ubicacionDao = new HibernateUbicacionDAO();
        espirituDAO = new HibernateEspirituDAO();
        mediumDAO = new HibernateMediumDAO();

        serviceU = new UbicacionServiceImpl(ubicacionDao);
        serviceE = new EspirituServiceImpl(espirituDAO, mediumDAO);
        serviceM = new MediumServiceImpl(mediumDAO, espirituDAO);

        quilmes = new Ubicacion("Quilmes");
        bernal = new Ubicacion("Bernal");

        angel = new EspirituAngelical("damian",quilmes);
        demonio = new EspirituDemoniaco("Roberto", quilmes);


        medium = new Medium("roberto", 200, 150, quilmes);

        serviceU.crear(quilmes);
        serviceU.crear(bernal);

        serviceEliminarTodo = new EliminarTodoServiceImpl(ubicacionDao, mediumDAO, espirituDAO);

    }

    @Test
    void espiritusEnUnaUbicacionExistente() {
        serviceE.guardar(angel);
        serviceE.guardar(demonio);

        List<Espiritu> espiritusEn = serviceU.espiritusEn(quilmes.getId());
        assertEquals(2, espiritusEn.size());
    }

    @Test
    void espiritusEnUnaUbicacionInexistente() {
        serviceE.guardar(angel);
        serviceE.guardar(demonio);

        List<Espiritu> espiritusEn = service.espiritusEn(bernal.getId());
        assertEquals(0, espiritusEn.size());
    }

    @Test
    void mediumsSinEspiritusEnUbicacion() {
        serviceM.crear(medium);
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(quilmes.getId());
        assertEquals(1, mediums.size());
        assertEquals(medium.getId(),mediums.getFirst().getId());
    }

    @Test
    void noHayMediumsEnBernal() {
        serviceM.crear(medium);
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(bernal.getId());
        assertEquals(0, mediums.size());
    }

    @Test
    void hayMediumsPeroTienenEspiritusDespuesDeConectarseEnQuilmes() {
        serviceE.guardar(angel);
        serviceM.crear(medium);
        serviceE.conectar(angel.getId(), medium.getId());
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(quilmes.getId());
        assertEquals(0, mediums.size());
    }

    @Test
    void noHayMediumsSinEspiritusEnUbicacionInexistente() {
        serviceM.crear(medium);
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(99L);
        assertEquals(0, mediums.size());
    }

    @Test
    void recuperarUbicacionDada() {
        Ubicacion q = serviceU.recuperar(quilmes.getId());
        assertEquals("Quilmes", q.getNombre());
    }

    @Test
    void recuperarTodasLasUbicaciones() {
        List<Ubicacion> ubicaciones = serviceU.recuperarTodos();
        assertEquals(2, ubicaciones.size());
    }

    @Test
    void actualizarUnaUbicacion(){
        Ubicacion q = serviceU.recuperar(quilmes.getId());
        q.cambiarNombre("Avellaneda");
        serviceU.actualizar(q);
        Ubicacion nombreNuevo = serviceU.recuperar(q.getId());

        assertEquals("Avellaneda", nombreNuevo.getNombre());
    }

    @Test
    void eliminarUbicacion() {
        serviceU.eliminar(quilmes);
        List<Ubicacion> ubicaciones = serviceU.recuperarTodos();
        assertEquals(1, ubicaciones.size());
    }

    @AfterEach
    void cleanup() {
        serviceEliminarTodo.eliminarTodo();
    }

}
