package ar.edu.unq.epersgeist.controller;
import ar.edu.unq.epersgeist.controller.dto.CreateMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.UpdateMediumDTO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
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
    private EspirituService espirituService;
    private MediumControllerREST controller;

    @BeforeEach
    void setUp() {
        mediumService = mock(MediumService.class);
        ubicacionService = mock(UbicacionService.class);
        espirituService = mock(EspirituService.class);
        controller = new MediumControllerREST(mediumService, ubicacionService, espirituService);
    }

    @Test
    void obtenerTodosLosMediums() {
        Medium medium = new Medium();
        when(mediumService.recuperarTodos()).thenReturn(List.of(medium));

        List<?> result = controller.getAllMediums();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mediumService, times(1)).recuperarTodos();
    }

    @Test
    void obtenerMediumPorIdEncontrado() {
        Medium medium = new Medium();
        when(mediumService.recuperar(1L)).thenReturn(Optional.of(medium));

        ResponseEntity<?> response = controller.getMediumById(1L);

        assertEquals(200, response.getStatusCodeValue());
        verify(mediumService, times(1)).recuperar(1L);
    }

    @Test
    void obtenerMediumPorIdNoEncontrado() {
        when(mediumService.recuperar(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.getMediumById(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(mediumService, times(1)).recuperar(1L);
    }
    
    @Test
    void actualizarMedium() {
        UpdateMediumDTO dto = mock(UpdateMediumDTO.class);
        Medium medium = new Medium();
        when(mediumService.recuperar(1L)).thenReturn(Optional.of(medium));
        when(mediumService.actualizar(medium)).thenReturn(medium);

        ResponseEntity<?> response = controller.updateById(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
        verify(mediumService, times(1)).recuperar(1L);
        verify(mediumService, times(1)).actualizar(medium);
    }

    @Test
    void actualizarMediumNoEncontrado() {
        UpdateMediumDTO dto = mock(UpdateMediumDTO.class);
        when(mediumService.recuperar(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.updateById(1L, dto);

        assertEquals(404, response.getStatusCodeValue());
        verify(mediumService, times(1)).recuperar(1L);
    }

    @Test
    void eliminarMediumExitoso() {
        Medium medium = new Medium();
        when(mediumService.recuperar(1L)).thenReturn(Optional.of(medium));

        ResponseEntity<?> response = controller.eliminar(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(mediumService, times(1)).recuperar(1L);
        verify(mediumService, times(1)).eliminar(1L);
    }

    @Test
    void crearMediumUbicacionNoEncontrada() {
        CreateMediumDTO dto = mock(CreateMediumDTO.class);
        when(ubicacionService.recuperar(dto.ubicacionId())).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.createMedium(dto);

        assertEquals(404, response.getStatusCodeValue());
        verify(ubicacionService, times(1)).recuperar(dto.ubicacionId());
    }

    @Test
    void exorcizarMediumEmisorNoEncontrado() {
        when(mediumService.recuperar(1L)).thenReturn(Optional.empty());
        when(mediumService.recuperar(2L)).thenReturn(Optional.of(new Medium()));

        ResponseEntity<?> response = controller.exorcizar(1L, 2L);

        assertEquals(404, response.getStatusCodeValue());
        verify(mediumService, times(1)).recuperar(1L);
        verify(mediumService, times(1)).recuperar(2L);
    }

    @Test
    void exorcizarMediumReceptorNoEncontrado() {
        when(mediumService.recuperar(1L)).thenReturn(Optional.of(new Medium()));
        when(mediumService.recuperar(2L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.exorcizar(1L, 2L);

        assertEquals(404, response.getStatusCodeValue());
        verify(mediumService, times(1)).recuperar(1L);
        verify(mediumService, times(1)).recuperar(2L);
    }

    @Test
    void descansarMediumNoEncontrado() {
        when(mediumService.recuperar(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.descansar(1L);

        assertEquals(404, response.getStatusCodeValue());
        verify(mediumService, times(1)).recuperar(1L);
    }
    @Test
    void moverMediumUbicacionNoEncontrada() {
        when(mediumService.recuperar(1L)).thenReturn(Optional.of(new Medium()));
        when(ubicacionService.recuperar(2L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.mover(1L, 2L);

        assertEquals(404, response.getStatusCodeValue());
        verify(mediumService, times(1)).recuperar(1L);
        verify(ubicacionService, times(1)).recuperar(2L);
    }
}
