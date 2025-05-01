package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.helper.MockMVCEspirituController;
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

   // @Autowired private MockMvcUbicacionController mockMvcUbicacionController;

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

        espirituId = mockMVCEspirituController.guardarEspiritu(espiritu1);
        mockMVCEspirituController.guardarEspiritu(espiritu2);

        espirituService.guardar(espiritu1);
        espirituService.guardar(espiritu2);
    }

    @Test
    void getEspirituByIdTest() throws Throwable{
        //mockMVCUbicacionController.guardarUbicacion(ubicacion1, HttpStatus.CREATED);

        var espirituRecuperado = mockMVCEspirituController.recuperarEspiritu(espirituId);

        assertEquals(espiritu1.getNombre(),espirituRecuperado.getNombre());

    }

    @Test
    void cantiTotalRecuperarTodosTest() throws Throwable{
        assertEquals(mockMVCEspirituController.recuperarTodos().size(), 2);
    }

//    @AfterEach
//    void eliminarTodo(){
//        serviceEliminarTodo.eliminarTodo();
//    }

}
