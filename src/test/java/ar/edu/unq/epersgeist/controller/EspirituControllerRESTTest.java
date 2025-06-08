package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.espiritu.CreateEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.CreateMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CoordenadaDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CreateUbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.PoligonoDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.controller.helper.MockMVCEspirituController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCMediumController;
import ar.edu.unq.epersgeist.controller.helper.MockMVCUbicacionController;
import ar.edu.unq.epersgeist.modelo.enums.Direccion;
import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;

import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EspirituControllerRESTTest {

    @Autowired private EspirituService espirituService;
    @Autowired private MediumService mediumService;
    @Autowired private UbicacionService ubicacionService;
    @Autowired private DataService dataService;

    @Autowired private UbicacionRepository ubicacionRepository;

    @Autowired private MediumDAOSQL mediumRepository;

    @Autowired private MockMVCUbicacionController mockMVCUbicacionController;
    @Autowired private MockMVCEspirituController mockMVCEspirituController;
    @Autowired private MockMVCMediumController mockMVCMediumController;

    private CreateUbicacionDTO quilmes;
    private CreateUbicacionDTO bernal;
    private CreateEspirituDTO angel;
    private CreateEspirituDTO demon;
    private CreateMediumDTO medium;
    private UbicacionDTO bernalGuardado;
    private UbicacionDTO quilmesGuardado;
    private EspirituDTO angelGuardado;
    private EspirituDTO demonGuardado;

    private PoligonoDTO poligono;
    private CoordenadaDTO c1;

    @BeforeEach
    void setUp() throws Throwable {
        dataService.eliminarTodo();
        c1 = new CoordenadaDTO(0.0, 0.0);
        List<CoordenadaDTO> coordenadasCuadrado = Arrays.asList(
                c1, // esquina inferior izquierda
                new CoordenadaDTO(0.0, 1.0), // esquina inferior derecha
                new CoordenadaDTO(1.0, 0.0), // esquina superior izquierda
                new CoordenadaDTO(1.0, 1.0), // esquina superior derecha
                new CoordenadaDTO(0.0, 0.0)  // cerrar el polígono
        );
        poligono = new PoligonoDTO(coordenadasCuadrado);

        quilmes = new CreateUbicacionDTO("Quilmes",50, TipoUbicacion.CEMENTERIO, poligono);
        bernal = new CreateUbicacionDTO("Bernal",50, TipoUbicacion.SANTUARIO, poligono);
        bernalGuardado = mockMVCUbicacionController.guardarUbicacion(bernal, UbicacionDTO.class);
        quilmesGuardado = mockMVCUbicacionController.guardarUbicacion(quilmes, UbicacionDTO.class);

        angel = new CreateEspirituDTO("angel", bernalGuardado.id(), TipoEspiritu.ANGELICAL, c1);
        demon = new CreateEspirituDTO("demon", quilmesGuardado.id(), TipoEspiritu.DEMONIACO, c1);

        angelGuardado = mockMVCEspirituController.guardarEspiritu(angel, EspirituDTO.class);
        demonGuardado = mockMVCEspirituController.guardarEspiritu(demon, EspirituDTO.class);



    }


    @Test
    void getEspirituByIdTest() throws Throwable{
        var espirituRecuperado = mockMVCEspirituController.getEspirituById(angelGuardado.id());

        assertEquals(espirituRecuperado.getNombre(), angelGuardado.nombre());
        assertEquals(angelGuardado.nivelDeConexion(),espirituRecuperado.getNivelDeConexion());
        assertEquals(angelGuardado.tipo(), espirituRecuperado.getTipo());

    }

    @Test
    void getRecuperarTodosTest() throws Throwable{
        assertEquals(2, mockMVCEspirituController.recuperarTodos().size());
    }

    @Test
    void eliminarTest() throws Throwable{
        mockMVCEspirituController.eliminar(angelGuardado.id());

        Collection<Espiritu> espiritus = mockMVCEspirituController.recuperarTodos();
        var espiritu = espiritus.stream().findAny().get();

        assertEquals(1, espiritus.size());
        assertEquals(espiritu.getNombre(), demonGuardado.nombre());
        assertEquals(espiritu.getTipo(), demonGuardado.tipo());
        assertEquals(espiritu.getNivelDeConexion(), demonGuardado.nivelDeConexion());
    }

    @Test
    public void testGetEspiritusDemoniacosPaginacion() throws Throwable {

        var status = mockMVCEspirituController.espiritusDemoniacos(
                Direccion.ASCENDENTE, 1,10, HttpStatus.OK);
        Assertions.assertEquals(HttpStatus.OK.value(), status);
    }

    @AfterEach
    void eliminarTodo(){
        dataService.eliminarTodo();
    }

}