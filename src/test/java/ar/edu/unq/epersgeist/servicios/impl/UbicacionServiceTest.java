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

    private UbicacionServiceImpl service;
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
    private GeneradorDeNumeros generadorMock;

    @BeforeEach
    void prepare() {
        ubicacionDao = new HibernateUbicacionDAO();
        espirituDAO = new HibernateEspirituDAO();
        mediumDAO = new HibernateMediumDAO();

        service = new UbicacionServiceImpl(ubicacionDao);
        serviceE = new EspirituServiceImpl(espirituDAO, mediumDAO);
        serviceM = new MediumServiceImpl(mediumDAO, espirituDAO);

        quilmes = new Ubicacion("Quilmes");
        bernal = new Ubicacion("Bernal");

        generadorMock = mock(GeneradorDeNumeros.class);

        angel = new EspirituAngelical(10,"damian",quilmes, generadorMock);
        demonio = new EspirituDemoniaco(15,"Roberto", quilmes, generadorMock);


        medium = new Medium("roberto", 200, 150, quilmes);

        service.crear(quilmes);
        service.crear(bernal);


    }

    @Test
    void espiritusEnUnaUbicacion() {
        serviceE.guardar(angel);
        serviceE.guardar(demonio);

        List<Espiritu> espiritusEn = service.espiritusEn(quilmes.getId());
        assertEquals(2, espiritusEn.size());
    }

    @Test
    void mediumsSinEspiritusEnUbicacion() {
        serviceM.crear(medium);
        List<Medium> mediums = service.mediumsSinEspiritusEn(quilmes.getId());
        assertEquals(1, mediums.size());
    }

    @Test
    void recuperarUbicacionDada() {
        Ubicacion q = service.recuperar(quilmes.getId());
        assertEquals("Quilmes", q.getNombre());
    }

    @Test
    void recuperarTodasLasUbicaciones() {
        List<Ubicacion> ubicaciones = service.recuperarTodos();
        assertEquals(2, ubicaciones.size());
    }

    @Test
    void actualizarUnaUbicacion(){
        Ubicacion q = service.recuperar(quilmes.getId());
        service.actualizar(q.getId(), "Avellaneda");
        Ubicacion nombreNuevo = service.recuperar(quilmes.getId());

        assertEquals("Avellaneda", nombreNuevo.getNombre());
    }

    @Test
    void eliminarUbicacion() {
        service.eliminar(quilmes);
        List<Ubicacion> ubicaciones = service.recuperarTodos();
        assertEquals(1, ubicaciones.size());
    }

    @Test
    void paginacionDeUbicacionTest(){

        //List<Ubicacion> espiritusEsperados = espirituDAO.recuperarPaginados(1,2);
        List<Ubicacion> espiritusServices = service.recuperarPaginados(1,2);

        assertEquals(2,espiritusServices.size());
        //assertEquals(2,espiritusEsperados.size());
        //assertEquals(espiritusEsperados,UbicacionServiceImpl);

    }

    @AfterEach
    void cleanup() {
        serviceE.eliminarTodo();
        serviceM.eliminarTodo();
        service.eliminarTodo();
    }

}
