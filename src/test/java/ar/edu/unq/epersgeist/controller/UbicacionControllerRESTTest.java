package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.helper.MockMVCEspirituController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCUbicacionController;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.dao.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.servicios.impl.DataServiceImpl;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Collection;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UbicacionControllerRESTTest {

    @Autowired private EspirituService espirituService;
    @Autowired private MediumService mediumService;
    @Autowired private UbicacionService ubicacionService;
    @Autowired private DataService serviceEliminarTodo;

    @Autowired private UbicacionDAO ubicacionDAO;
    @Autowired private EspirituDAO espirituDAO;
    @Autowired private MediumDAO mediumDAO;

    @Autowired private MockMVCUbicacionController mockMVCUbicacionController;
    @Autowired private MockMVCEspirituController mockMVCEspirituController;
    //@Autowired private MockMvcMediumController mockMVCMediumController;

    private Ubicacion quilmes;
    private Ubicacion bernal;
    private Ubicacion quilmesGuardado;
    private Ubicacion bernalGuardado;
    private Espiritu espiritu1;
    private Espiritu espiritu2;

    private Long espirituId;
    private Long espiritu2Id;

    private Medium medium;




    @BeforeEach
    void setUp() throws Throwable {
        serviceEliminarTodo.eliminarTodo();
        quilmes = new Cementerio("Cementerio 1", 50);
        bernal = new Santuario("Santuario 1", 50);

        bernalGuardado = mockMVCUbicacionController.guardarUbicacion(bernal, Santuario.class);
        quilmesGuardado = mockMVCUbicacionController.guardarUbicacion(quilmes, Cementerio.class);

        /*
        medium = new Medium("medium",100,20,quilmes);

        mediumService.guardar(medium);
        */
    /*
        espiritu1 = new EspirituAngelical("angelical 1", quilmesGuardado);
        espiritu2 = new EspirituAngelical("angelical 2", bernalGuardado);


        espirituId = mockMVCEspirituController.guardarEspiritu(espiritu1);
        espiritu2Id = mockMVCEspirituController.guardarEspiritu(espiritu2);

     */
    }

    @Test
    void getUbicacionesTest() throws Throwable{
        var ubicaciones = mockMVCUbicacionController.getUbicaciones();
        assertEquals(2, ubicaciones.size());
    }

    @Test
    void getUbicacionByIdTest() throws Throwable{
        var ubiRecuperada = mockMVCUbicacionController.getUbicacionById(quilmesGuardado.getId());

        assertEquals(quilmes.getNombre(),ubiRecuperada.getNombre());
        assertEquals(quilmes.getFlujoDeEnergia(),ubiRecuperada.getFlujoDeEnergia());
        assertEquals(quilmes.getTipo(), ubiRecuperada.getTipo());
    }
    /*
    @Test
    void getEspiritusEnTest() throws Throwable{
        assertEquals(1, mockMVCUbicacionController.getEspiritusEn(bernalGuardado.getId()).size());
        assertEquals(1, mockMVCUbicacionController.getEspiritusEn(quilmesGuardado.getId()).size());
    }
    */
    @Test
    void eliminarTest() throws Throwable{
        mockMVCUbicacionController.eliminar(quilmesGuardado.getId());

        Collection<Ubicacion> ubicaciones = mockMVCUbicacionController.getUbicaciones();
        var ubi = ubicaciones.stream().findAny().get();

        assertEquals(1, ubicaciones.size());
        assertEquals(ubi.getNombre(), bernalGuardado.getNombre());
        assertEquals(ubi.getTipo(), bernalGuardado.getTipo());
        assertEquals(ubi.getFlujoDeEnergia(), bernalGuardado.getFlujoDeEnergia());
    }

    @AfterEach
    void eliminarTodo(){
        serviceEliminarTodo.eliminarTodo();
    }

}
