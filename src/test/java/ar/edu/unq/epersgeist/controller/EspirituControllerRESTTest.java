package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.helper.MockMVCEspirituController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCUbicacionController;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.impl.DataServiceImpl;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
public class EspirituControllerRESTTest {

    @Autowired private EspirituService espirituService;
    @Autowired private MediumService mediumService;
    @Autowired private UbicacionService ubicacionService;

    @Autowired private UbicacionDAO ubicacionDAO;
    @Autowired private EspirituDAO espirituDAO;
    @Autowired private MediumDAO mediumDAO;

    @Autowired private MockMVCEspirituController mockMVCEspirituController;

    @Autowired private MockMVCUbicacionController mockMvcUbicacionController;

    private Ubicacion quilmes;
    private Ubicacion bernal;

    private Espiritu espiritu1;
    private Espiritu espiritu2;

    private Long espirituId;

    private Medium medium;


    private DataService serviceEliminarTodo;

    @BeforeEach
    void setUp() throws Throwable {
        serviceEliminarTodo = new DataServiceImpl(ubicacionDAO, mediumDAO, espirituDAO);
        serviceEliminarTodo.eliminarTodo();

        quilmes = new Santuario("Santuario 1", 50);
        bernal = new Santuario("Santuario 2", 50);

        ubicacionService.guardar(quilmes);
        ubicacionService.guardar(bernal);

        medium = new Medium("medium",100,20,quilmes);

        mediumService.guardar(medium);

        espiritu1 = new EspirituAngelical("angelical 1", quilmes);
        espiritu2 = new EspirituAngelical("angelical 2", bernal);

        espirituId = mockMVCEspirituController.guardar(espiritu1);
        mockMVCEspirituController.guardar(espiritu2);



    }


    @Test
    void getEspirituByIdTest() throws Throwable{
        var espirituRecuperado = mockMVCEspirituController.recuperarEspiritu(espirituId);

        assertEquals(espirituRecuperado.getNombre(), espiritu1.getNombre());
        assertEquals(espiritu1.getNivelDeConexion(),espirituRecuperado.getNivelDeConexion());
        assertEquals(espiritu1.getTipo(), espirituRecuperado.getTipo());

    }

    @Test
    void getRecuperarTodosTest() throws Throwable{
        assertEquals(2, mockMVCEspirituController.recuperarTodos().size());
    }

    @Test
    void eliminarTest() throws Throwable{
        mockMVCEspirituController.eliminar(espirituId);

        Collection<Espiritu> espiritus = mockMVCEspirituController.recuperarTodos();
        var espiritu = espiritus.stream().findAny().get();

        assertEquals(1, espiritus.size());
        assertEquals(espiritu.getNombre(), espiritu2.getNombre());
        assertEquals(espiritu.getTipo(), espiritu2.getTipo());
        assertEquals(espiritu.getNivelDeConexion(), espiritu2.getNivelDeConexion());
    }

    @Test
    public void testGetEspiritusDemoniacosPaginacion() throws Throwable {
        var dem = new EspirituDemoniaco("Demonio", bernal);
        mockMVCEspirituController.guardar(dem);

        var status = mockMVCEspirituController.espiritusDemoniacos(
                Direccion.ASCENDENTE, 1,10, HttpStatus.OK);
        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @AfterEach
    void eliminarTodo(){
        serviceEliminarTodo.eliminarTodo();
    }

}
