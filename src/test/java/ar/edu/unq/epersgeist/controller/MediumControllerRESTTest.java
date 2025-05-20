package ar.edu.unq.epersgeist.controller;
import ar.edu.unq.epersgeist.controller.dto.CreateMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.UpdateMediumDTO;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.exception.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MediumControllerRESTTest {

    private MediumService mediumService;
    private UbicacionService ubicacionService;
    private MediumControllerREST controller;
    private Medium medium;
    private Ubicacion ubicacion;

    @BeforeEach
    void setUp() {
        mediumService = mock(MediumService.class);
        ubicacionService = mock(UbicacionService.class);
        controller = new MediumControllerREST(mediumService, ubicacionService);

        medium = mock(Medium.class);
        ubicacion = mock(Ubicacion.class);
    }

    @Test
    void obtenerTodosLosMediums() {
        when(mediumService.recuperarTodos()).thenReturn(List.of(medium));
        when(medium.getUbicacion()).thenReturn(ubicacion);

        List<?> result = controller.getAllMediums();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mediumService, times(1)).recuperarTodos();
    }

    @Test
    void obtenerMediumPorIdEncontrado() {
        when(mediumService.recuperar(1L)).thenReturn(Optional.of(medium));
        when(medium.getUbicacion()).thenReturn(ubicacion);

        ResponseEntity<?> response = controller.getMediumById(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(mediumService, times(1)).recuperar(1L);
    }

    @Test
    void obtenerMediumPorIdNoEncontrado() {
        when(mediumService.recuperar(1L)).thenReturn(Optional.empty());

        assertThrows(MediumNoEncontradoException.class, () -> controller.getMediumById(1L));

        verify(mediumService, times(1)).recuperar(1L);
    }
    
    @Test
    void actualizarMedium() {
        UpdateMediumDTO dto = mock(UpdateMediumDTO.class);
        when(medium.getUbicacion()).thenReturn(ubicacion);
        when(medium.getId()).thenReturn(1L);
        when(mediumService.actualizar(medium)).thenReturn(medium);
        when(mediumService.recuperar(medium.getId())).thenReturn(Optional.of(medium));

        ResponseEntity<?> response = controller.updateById(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
        verify(mediumService, times(1)).recuperar(1L);
        verify(mediumService, times(1)).actualizar(medium);
    }

    @Test
    void actualizarMediumNoEncontrado() {
        UpdateMediumDTO dto = mock(UpdateMediumDTO.class);
        when(mediumService.recuperar(1L)).thenReturn(Optional.empty());

        assertThrows(MediumNoEncontradoException.class, () -> controller.updateById(1L, dto));

        verify(mediumService, times(1)).recuperar(1L);
    }

    @Test
    void eliminarMediumExitoso() {
        Medium medium = new Medium();
        when(mediumService.recuperar(1L)).thenReturn(Optional.of(medium));

        ResponseEntity<?> response = controller.eliminar(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(mediumService, times(1)).eliminar(1L);
    }

    @Test
    void crearMediumUbicacionNoEncontrada() {
        CreateMediumDTO dto = mock(CreateMediumDTO.class);
        when(ubicacionService.recuperar(dto.ubicacionId())).thenReturn(Optional.empty());

        assertThrows(UbicacionNoEncontradaException.class, () -> controller.createMedium(dto));

        verify(ubicacionService, times(1)).recuperar(dto.ubicacionId());
    }

    @Test
    void exorcizarMedium() {
        controller.exorcizar(1L, 2L);

        verify(mediumService, times(1)).exorcizar(1L, 2L);
    }
}
