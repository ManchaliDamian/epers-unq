package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.espiritu.CreateEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CoordenadaDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CreateUbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.PoligonoDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.controller.helper.MockMVCEspirituController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCUbicacionController;
import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UbicacionControllerRESTTest {

    @Autowired private EspirituService espirituService;
    @Autowired private MediumService mediumService;
    @Autowired private UbicacionService ubicacionService;
    @Autowired private DataService dataService;

    @Autowired private MockMVCUbicacionController mockMVCUbicacionController;
    @Autowired private MockMVCEspirituController mockMVCEspirituController;

    private CreateUbicacionDTO quilmes;
    private CreateUbicacionDTO bernal;
    private CreateEspirituDTO angel;
    private CreateEspirituDTO demon;
    private UbicacionDTO bernalGuardado;
    private UbicacionDTO quilmesGuardado;
    private EspirituDTO angelGuardado;
    private EspirituDTO demonGuardado;

    private CoordenadaDTO c1;
    private CoordenadaDTO c4;
    private CoordenadaDTO c3;
    private CoordenadaDTO c2;
    private CoordenadaDTO c5;
    private CoordenadaDTO c6;
    private CoordenadaDTO c7;
    private CoordenadaDTO c8;
    private PoligonoDTO poligono;
    private PoligonoDTO poligono1;

    @BeforeEach
    void setUp() throws Throwable {
        dataService.eliminarTodo();
        c1 = new CoordenadaDTO(0.0,0.0);
        c2 = new CoordenadaDTO(0.0,1.0);
        c3 = new CoordenadaDTO(1.0,1.0);
        c4 = new CoordenadaDTO(1.0,0.0);
        List<CoordenadaDTO> coordenadas = Arrays.asList(c1, c2, c3, c4, c1);
        poligono = new PoligonoDTO(coordenadas);

        c5 = new CoordenadaDTO(2.0,2.0);
        c6 = new CoordenadaDTO(2.0,3.0);
        c7 = new CoordenadaDTO(3.0,3.0);
        c8 = new CoordenadaDTO(3.0,2.0);
        List<CoordenadaDTO> coordenadas1 = Arrays.asList(c5, c6, c7, c8, c5);
        poligono1 = new PoligonoDTO(coordenadas1);

        quilmes = new CreateUbicacionDTO("Quilmes",50, TipoUbicacion.CEMENTERIO, poligono);
        bernal = new CreateUbicacionDTO("Bernal",50, TipoUbicacion.SANTUARIO, poligono1);
        bernalGuardado = mockMVCUbicacionController.guardarUbicacion(bernal, UbicacionDTO.class);
        quilmesGuardado = mockMVCUbicacionController.guardarUbicacion(quilmes, UbicacionDTO.class);

        angel = new CreateEspirituDTO("angel", bernalGuardado.id(), TipoEspiritu.ANGELICAL, c5, 30, 10);
        demon = new CreateEspirituDTO("demon", quilmesGuardado.id(), TipoEspiritu.DEMONIACO, c1, 30, 10);

        angelGuardado = mockMVCEspirituController.guardarEspiritu(angel, EspirituDTO.class);
        demonGuardado = mockMVCEspirituController.guardarEspiritu(demon, EspirituDTO.class);

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
        dataService.eliminarTodo();
    }

}