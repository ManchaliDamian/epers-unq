package ar.edu.unq.epersgeist.controller;
import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.controller.helper.MockMVCMediumController;
import ar.edu.unq.epersgeist.modelo.*;
import ar.edu.unq.epersgeist.persistencia.dao.*;
import ar.edu.unq.epersgeist.servicios.interfaces.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@AutoConfigureMockMvc
public class MediumControllerRESTTest {

    @Autowired private MediumService mediumService;
    @Autowired private UbicacionService ubicacionService;
    @Autowired private EspirituService espirituService;
    @Autowired private DataService serviceEliminarTodo;

    @Autowired private UbicacionDAO ubicacionDAO;
    @Autowired private MediumDAO mediumDAO;
    @Autowired private EspirituDAO espirituDAO;

    @Autowired private MockMVCMediumController mockMVCMediumController;

    private CreateUbicacionDTO quilmes;
    private CreateUbicacionDTO bernal;
    private CreateMediumDTO medium;
    private UbicacionDTO quilmesGuardado;
    private UbicacionDTO bernalGuardado;
    private MediumDTO mediumGuardado;

    @BeforeEach
    void setUp() throws Throwable {
        serviceEliminarTodo.eliminarTodo();
        quilmes = new CreateUbicacionDTO("Quilmes", 50, TipoUbicacion.CEMENTERIO);
        bernal = new CreateUbicacionDTO("Bernal", 50, TipoUbicacion.SANTUARIO);
        quilmesGuardado = mockMVCMediumController.guardarUbicacion(quilmes, UbicacionDTO.class);
        bernalGuardado = mockMVCMediumController.guardarUbicacion(bernal, UbicacionDTO.class);

        medium = new CreateMediumDTO("Medium1", 1L, 120, 12, Set.of());
        mediumGuardado = mockMVCMediumController.guardarMedium(medium, MediumDTO.class);
    }

    @Test
    void getMediumByIdTest() throws Throwable {
        var mediumRecuperado = mockMVCMediumController.getMediumById(mediumGuardado.id());

        assertEquals(mediumGuardado.nombre(), mediumRecuperado.getNombre());
        assertEquals(mediumGuardado.ubicacion().getId(), mediumRecuperado.getUbicacion().getId());
    }

    @Test
    void getAllMediumsTest() throws Throwable {
        assertEquals(1, mockMVCMediumController.recuperarTodos().size());
    }

    @Test
    void eliminarMediumTest() throws Throwable {
        mockMVCMediumController.eliminar(mediumGuardado.id());
        Collection<Medium> mediums = mockMVCMediumController.recuperarTodos();

        // Validar que está marcado como eliminado
        assertTrue(mediums.stream().noneMatch(m -> m.getId().equals(mediumGuardado.id())));
    }

    @Test
    void updateMediumTest() throws Throwable {
        UpdateMediumDTO updateMediumDTO = new UpdateMediumDTO("UpdatedMedium", bernalGuardado.id());
        var updatedMedium = mockMVCMediumController.actualizarMedium(mediumGuardado.id(), updateMediumDTO);

        assertEquals("UpdatedMedium", updatedMedium.getNombre());
        assertEquals(bernalGuardado.id(), updatedMedium.getUbicacion().getId());
    }


    @Test
    void testExorcizarMedium() throws Throwable {
        CreateMediumDTO medium2 = new CreateMediumDTO("Medium2", quilmesGuardado.id(), 120, 12, Set.of());
        MediumDTO medium2Guardado = mockMVCMediumController.guardarMedium(medium2, MediumDTO.class);

        // Cargar espíritus
        EspirituAngelical angel = new EspirituAngelical("Angel", 10, "aaa");
        EspirituDemoniaco demonio = new EspirituDemoniaco("Demon", 10, "bbb");

        angel.setUbicacion(ubicacionDAO.findById(quilmesGuardado.id()).get());
        demonio.setUbicacion(ubicacionDAO.findById(quilmesGuardado.id()).get());

        espirituService.guardar(angel);
        espirituService.guardar(demonio);

        espirituService.invocar(mediumGuardado.id(), angel.getId());
        espirituService.invocar(medium2Guardado.id(), demonio.getId());

        var response = mockMVCMediumController.exorcizar(mediumGuardado.id(), medium2Guardado.id());
        assertEquals(HttpStatus.OK.value(), response);
    }

    @Test
    void testDescansarMedium() throws Throwable {
        var response = mockMVCMediumController.descansar(mediumGuardado.id());
        assertEquals(HttpStatus.OK.value(), response);
    }

    @AfterEach
    void eliminarTodo() {
        serviceEliminarTodo.eliminarTodo();
    }
}