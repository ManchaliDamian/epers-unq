package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEliminableException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionesNoConectadasException;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;

import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionNeoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UbicacionServiceTest {

    @Autowired private MediumService serviceM;
    @Autowired private EspirituService serviceE;
    @Autowired private UbicacionService serviceU;

    @Autowired private MediumRepository mediumRepository;
    @Autowired private EspirituRepository espirituRepository;
    @Autowired private UbicacionRepository ubicacionRepository;
    @Autowired private DataService dataService;


    private Medium medium;
    private Medium medium2;
    private Medium medium3;
    private Ubicacion santuario;
    private Ubicacion cementerio;
    private Espiritu angel;
    private Espiritu demonio;


    @BeforeEach
    void prepare() {

        santuario = new Santuario("Quilmes", 70);
        cementerio = new Cementerio("Bernal",60);

        angel = new EspirituAngelical("damian",santuario);
        demonio = new EspirituDemoniaco("Roberto", santuario);

        medium = new Medium("roberto", 200, 150, santuario);
        medium2 = new Medium("roberto", 200, 150, santuario);
        medium3 = new Medium("roberto", 200, 150, santuario);
        santuario = serviceU.guardar(santuario);
        cementerio = serviceU.guardar(cementerio);

    }

    @Test
    void testUpdateATDeUbicacion(){
        String nuevoNombre = "Nueva ubicacion";

        santuario.setNombre(nuevoNombre);
        santuario.setFlujoDeEnergia(35);
        santuario = serviceU.actualizar(santuario);

        Date fechaEsperada = new Date();

        Date fechaEspiritu = santuario.getUpdatedAt();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String esperadaFormateada = sdf.format(fechaEsperada);
        String obtenidaFormateada = sdf.format(fechaEspiritu);

        assertEquals(esperadaFormateada, obtenidaFormateada);
        assertEquals(nuevoNombre, santuario.getNombre());
        assertEquals(35,santuario.getFlujoDeEnergia());

    }

    @Test
    void testCreateAtDeUbicacion(){
        santuario = serviceU.guardar(santuario);

        Date fechaEsperada = new Date();

        Date fechaEspiritu = santuario.getCreatedAt();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String esperadaFormateada = sdf.format(fechaEsperada);
        String obtenidaFormateada = sdf.format(fechaEspiritu);

        assertEquals(esperadaFormateada, obtenidaFormateada);

    }

    @Test
    void recuperarUbicacionEliminada() {
        serviceU.eliminar(santuario.getId());
        Optional<Ubicacion> ubicacionEliminada = dataService.recuperarEliminadoUbicacion(santuario.getId());
        assertTrue(ubicacionEliminada.get().isDeleted());
    }
    @Test
    void recuperarTodasUbicacionesEliminadas() {
        serviceU.eliminar(santuario.getId());
        List<Ubicacion> ubicacionesEliminadas = dataService.recuperarTodosEliminadosDeUbicacion();
        assertEquals(1, ubicacionesEliminadas.size());
    }
    @Test
    void espiritusEnUnaUbicacionExistente() {
        serviceE.guardar(angel);
        serviceE.guardar(demonio);

        List<Espiritu> espiritusEn = serviceU.espiritusEn(santuario.getId());
        assertEquals(2, espiritusEn.size());
    }
    @Test
    void espiritusEnUnaUbicacionExistenteSinEliminados() {
        angel = serviceE.guardar(angel);
        serviceE.guardar(demonio);
        serviceE.eliminar(angel.getId());
        List<Espiritu> espiritusEn = serviceU.espiritusEn(santuario.getId());
        assertEquals(1, espiritusEn.size());
    }

    @Test
    void espiritusEnUnaUbicacionInexistente() {
        serviceE.guardar(angel);
        serviceE.guardar(demonio);

        List<Espiritu> espiritusEn = serviceU.espiritusEn(cementerio.getId());
        assertEquals(0, espiritusEn.size());
    }

    @Test
    void mediumsSinEspiritusEnUbicacion() {
        medium = serviceM.guardar(medium);
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(santuario.getId());
        assertEquals(1, mediums.size());
        assertEquals(medium.getId(),mediums.getFirst().getId());
    }

    @Test
    void noHayMediumsEnBernal() {
        serviceM.guardar(medium);
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(cementerio.getId());
        assertEquals(0, mediums.size());
    }

    @Test
    void hayMediumsPeroTienenEspiritusDespuesDeConectarseEnQuilmes() {
        angel = serviceE.guardar(angel);
        medium = serviceM.guardar(medium);
        serviceE.conectar(angel.getId(), medium.getId());
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(santuario.getId());
        assertEquals(0, mediums.size());
    }
    @Test
    void mediumEliminadoEnSantuario() {
        medium = serviceM.guardar(medium);
        serviceM.eliminar(medium.getId());
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(santuario.getId());
        assertEquals(0, mediums.size());
    }
    @Test
    void hayMediumsConUnMediumEliminadoEnSantuario() {
        medium = serviceM.guardar(medium);
        serviceM.eliminar(medium.getId());
        serviceM.guardar(medium2);
        serviceM.guardar(medium3);
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(santuario.getId());
        assertEquals(2, mediums.size());
    }

    @Test
    void noHayMediumsSinEspiritusEnUbicacionInexistente() {
        serviceM.guardar(medium);
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(99L);
        assertEquals(0, mediums.size());
    }

    @Test
    void recuperarUbicacionDada() {
        Optional<Ubicacion> q = serviceU.recuperar(santuario.getId());
        assertEquals("Quilmes", q.get().getNombre());
    }

    @Test
    public void testCrearYRecuperarUbicacion() {
        Optional<Ubicacion> recuperada = serviceU.recuperar(santuario.getId());

        assertNotNull(recuperada, "La ubicación recuperada no debería ser null");
        assertEquals("Quilmes", recuperada.get().getNombre(), "El nombre no coincide");
        assertEquals(santuario.getId(), recuperada.get().getId(), "El ID no coincide");
    }

    @Test
    void recuperarTodasLasUbicaciones() {
        List<Ubicacion> ubicaciones = serviceU.recuperarTodos();
        assertEquals(2, ubicaciones.size());
    }

    @Test
    void recuperarSantuariosExistentes(){
        List<Santuario> santuarios = serviceU.recuperarSantuarios();
        assertEquals(1, santuarios.size());
    }

    @Test
    void recuperarCementeriosExistentes(){
        List<Cementerio> cementerios = serviceU.recuperarCementerios();
        assertEquals(1, cementerios.size());
    }

    @Test
    void recuperarSantuariosNoExistentes(){
        serviceU.eliminar(santuario.getId());
        List<Santuario> santuarios = serviceU.recuperarSantuarios();
        assertEquals(0, santuarios.size());
    }

    @Test
    void recuperarCementeriosNoExistentes(){
        serviceU.eliminar(cementerio.getId());
        List<Cementerio> cementerios = serviceU.recuperarCementerios();
        assertEquals(0, cementerios.size());
    }


    @Test
    void actualizarUnaUbicacion(){
        Optional<Ubicacion> q = serviceU.recuperar(santuario.getId());
        q.get().cambiarNombre("Avellaneda");
        serviceU.guardar(q.get());
        Optional<Ubicacion> nombreNuevo = serviceU.recuperar(q.get().getId());

        assertEquals("Avellaneda", nombreNuevo.get().getNombre());
    }

    @Test
    void eliminarUbicacion() {
        serviceU.eliminar(santuario.getId());
        List<Ubicacion> ubicaciones = serviceU.recuperarTodos();
        assertEquals(1, ubicaciones.size());
    }
    @Test
    void eliminarUbicacionLanzaExceptionPorQueExisteUnEspirituEnEsaUbicacion() {

        serviceE.guardar(angel);

        assertThrows(UbicacionNoEliminableException.class, () -> serviceU.eliminar(santuario.getId()));
    }
    @Test
    void eliminarUbicacionLanzaExceptionPorQueExisteUnMediumEnEsaUbicacion() {

        serviceM.guardar(medium);

        assertThrows(UbicacionNoEliminableException.class, () -> serviceU.eliminar(santuario.getId()));
    }

    //-------------------------------------------------------------------------------------

    @Test
    void estanConectadas_esFalse_entreNodosNoEnlazados() {
            assertFalse(serviceU.estanConectadas(santuario.getId(), cementerio.getId()),
                    "Dos nodos sin relación no deberian estar conectados");
    }

    @Test
    void caminoMasCortoSinConexion_debeLanzarUbicacionesNoConectadasException() {
        assertThrows(
                UbicacionesNoConectadasException.class,
                () -> serviceU.caminoMasCorto(santuario.getId(), cementerio.getId()),
                "Si no hay ninguna arista, caminoMasCorto() debe lanzar la excepción"
        );
    }

    //Dudoso para probar cuando anden los test.

    @Test
    void verificarQueEstanConectadosDosUbicaciones(){
        Long idOrigen = santuario.getId();
        Long idDestino = cementerio.getId();
        assertTrue(serviceU.estanConectadas(idOrigen,idDestino));
    }

    @Test
    void caminoMasCortoEntreDosUbicacionesTest(){
        List<Ubicacion> ubicaciones = serviceU.caminoMasCorto(santuario.getId(),cementerio.getId());
        assertEquals(ubicaciones.size(),1);
        assertEquals(ubicaciones.getFirst().getNombre(),santuario.getNombre());
    }
    @Test
    void retornarMismaId(){

        Optional<Ubicacion> neo = serviceU.recuperar(santuario.getId());
        assertEquals(santuario.getId(), neo.get().getId());
        assertEquals(0, neo.get().getConexiones().size());
    }
    //-------------------------------------------------------------------------------------

//    @AfterEach
    void cleanup() {
        dataService.eliminarTodo();
    }

}
