package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.CreateEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.CreateUbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.controller.helper.MockMVCEspirituController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCUbicacionController;
import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;

import ar.edu.unq.epersgeist.persistencia.repositorys.interfaces.UbicacionRepository;
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

import java.util.Collection;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UbicacionControllerRESTTest {

    @Autowired private EspirituService espirituService;
    @Autowired private MediumService mediumService;
    @Autowired private UbicacionService ubicacionService;
    @Autowired private DataService serviceEliminarTodo;

    @Autowired private UbicacionRepository ubicacionDAO;
    @Autowired private EspirituDAO espirituDAO;
    @Autowired private MediumDAO mediumDAO;

    @Autowired private MockMVCUbicacionController mockMVCUbicacionController;
    @Autowired private MockMVCEspirituController mockMVCEspirituController;
    //@Autowired private MockMvcMediumController mockMVCMediumController;

    private CreateUbicacionDTO quilmes;
    private CreateUbicacionDTO bernal;
    private CreateEspirituDTO angel;
    private CreateEspirituDTO demon;
    //private CreateMediumDTO medium;
    private UbicacionDTO bernalGuardado;
    private UbicacionDTO quilmesGuardado;
    private EspirituDTO angelGuardado;
    private EspirituDTO demonGuardado;




    @BeforeEach
    void setUp() throws Throwable {
        serviceEliminarTodo.eliminarTodo();
        quilmes = new CreateUbicacionDTO("Quilmes",50, TipoUbicacion.CEMENTERIO);
        bernal = new CreateUbicacionDTO("Bernal",50, TipoUbicacion.SANTUARIO);
        bernalGuardado = mockMVCUbicacionController.guardarUbicacion(bernal, UbicacionDTO.class);
        quilmesGuardado = mockMVCUbicacionController.guardarUbicacion(quilmes, UbicacionDTO.class);

        angel = new CreateEspirituDTO("angel", bernalGuardado.id(), TipoEspiritu.ANGELICAL);
        demon = new CreateEspirituDTO("demon", quilmesGuardado.id(), TipoEspiritu.DEMONIACO);

        angelGuardado = mockMVCEspirituController.guardarEspiritu(angel, EspirituDTO.class);
        demonGuardado = mockMVCEspirituController.guardarEspiritu(demon, EspirituDTO.class);
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
        var ubiRecuperada = mockMVCUbicacionController.getUbicacionById(quilmesGuardado.id());

        assertEquals(quilmes.nombre(), ubiRecuperada.getNombre());
        assertEquals(quilmes.flujoDeEnergia(), ubiRecuperada.getFlujoDeEnergia());
        assertEquals(quilmes.tipo(), ubiRecuperada.getTipo());
    }

    @Test
    void getEspiritusEnTest() throws Throwable{
        assertEquals(1, mockMVCUbicacionController.getEspiritusEn(bernalGuardado.id()).size());
        assertEquals(1, mockMVCUbicacionController.getEspiritusEn(quilmesGuardado.id()).size());
    }

    @Test
    void eliminarTest() throws Throwable{
        mockMVCEspirituController.eliminar(demonGuardado.id());
        mockMVCUbicacionController.eliminar(quilmesGuardado.id());

        Collection<Ubicacion> ubicaciones = mockMVCUbicacionController.getUbicaciones();
        var ubi = ubicaciones.stream().findAny().get();

        assertEquals(1, ubicaciones.size());
        assertEquals(ubi.getNombre(), bernalGuardado.nombre());
        assertEquals(ubi.getTipo(), bernalGuardado.tipo());
        assertEquals(ubi.getFlujoDeEnergia(), bernalGuardado.flujoDeEnergia());
    }

    @AfterEach
    void eliminarTodo(){
        serviceEliminarTodo.eliminarTodo();
    }

}