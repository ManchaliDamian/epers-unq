package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.*;

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
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.Ubicacion;


import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class UbicacionServiceTest {

    @Autowired private MediumService serviceM;
    @Autowired private EspirituService serviceE;
    @Autowired private UbicacionService serviceU;

    @Autowired private MediumDAO mediumDAO;
    @Autowired private EspirituDAO espirituDAO;
    @Autowired private UbicacionDAO ubicacionDAO;

    private Medium medium;
    private Ubicacion quilmes;
    private Ubicacion bernal;
    private Espiritu angel;
    private Espiritu demonio;

    private DataService eliminarTodo;

    @BeforeEach
    void prepare() {
        quilmes = new Ubicacion("Quilmes");
        bernal = new Ubicacion("Bernal");

        angel = new EspirituAngelical("damian",quilmes);
        demonio = new EspirituDemoniaco("Roberto", quilmes);

        medium = new Medium("roberto", 200, 150, quilmes);

        serviceU.guardar(quilmes);
        serviceU.guardar(bernal);

        eliminarTodo = new DataServiceImpl(ubicacionDAO, mediumDAO, espirituDAO);

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

        List<Espiritu> espiritusEn = serviceU.espiritusEn(bernal.getId());
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
        Optional<Ubicacion> q = serviceU.recuperar(quilmes.getId());
        assertEquals("Quilmes", q.get().getNombre());
    }

    @Test
    public void testCrearYRecuperarUbicacion() {
        Optional<Ubicacion> recuperada = serviceU.recuperar(quilmes.getId());

        assertNotNull(recuperada, "La ubicación recuperada no debería ser null");
        assertEquals("Quilmes", recuperada.get().getNombre(), "El nombre no coincide");
        assertEquals(quilmes.getId(), recuperada.get().getId(), "El ID no coincide");
    }

    @Test
    void recuperarTodasLasUbicaciones() {
        List<Ubicacion> ubicaciones = serviceU.recuperarTodos();
        assertEquals(2, ubicaciones.size());
    }

    @Test
    void actualizarUnaUbicacion(){
        Optional<Ubicacion> q = serviceU.recuperar(quilmes.getId());
        q.get().cambiarNombre("Avellaneda");
        serviceU.guardar(q.get());
        Optional<Ubicacion> nombreNuevo = serviceU.recuperar(q.get().getId());

        assertEquals("Avellaneda", nombreNuevo.get().getNombre());
    }

    @Test
    void eliminarUbicacion() {
        serviceU.eliminar(quilmes);
        List<Ubicacion> ubicaciones = serviceU.recuperarTodos();
        assertEquals(1, ubicaciones.size());
    }

    @AfterEach
    void cleanup() {
        eliminarTodo.eliminarTodo();
    }

}
