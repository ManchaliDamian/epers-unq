package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.exception.MismaUbicacionException;
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
        cementerio = new Cementerio("Bernal", 60);

        angel = new EspirituAngelical("damian", santuario);
        demonio = new EspirituDemoniaco("Roberto", santuario);

        medium = new Medium("roberto", 200, 150, santuario);
        medium2 = new Medium("roberto", 200, 150, santuario);
        medium3 = new Medium("roberto", 200, 150, santuario);
        santuario = serviceU.guardar(santuario);
        cementerio = serviceU.guardar(cementerio);

    }

    @Test
    void testUpdateATDeUbicacion() {
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
        assertEquals(35, santuario.getFlujoDeEnergia());

    }

    @Test
    void testCreateAtDeUbicacion() {
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
        assertEquals(medium.getId(), mediums.getFirst().getId());
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
    void recuperarSantuariosExistentes() {
        List<Santuario> santuarios = serviceU.recuperarSantuarios();
        assertEquals(1, santuarios.size());
    }

    @Test
    void recuperarCementeriosExistentes() {
        List<Cementerio> cementerios = serviceU.recuperarCementerios();
        assertEquals(1, cementerios.size());
    }

    @Test
    void recuperarSantuariosNoExistentes() {
        serviceU.eliminar(santuario.getId());
        List<Santuario> santuarios = serviceU.recuperarSantuarios();
        assertEquals(0, santuarios.size());
    }

    @Test
    void recuperarCementeriosNoExistentes() {
        serviceU.eliminar(cementerio.getId());
        List<Cementerio> cementerios = serviceU.recuperarCementerios();
        assertEquals(0, cementerios.size());
    }


    @Test
    void actualizarUnaUbicacion() {
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

    //-----NEO---------------------------------------------------------------------------

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

    @Test
    void verificarQueEstanConectadasDespuesDeConectar() {
        serviceU.conectar(santuario.getId(), cementerio.getId());
        assertTrue(
                serviceU.estanConectadas(santuario.getId(), cementerio.getId()),
                "Después de conectar, deben reportarse como conectadas"
        );
    }

    @Test
    void conectarUbicacionConsigoMisma_debeLanzarMismaUbicacionException() {
        assertThrows(MismaUbicacionException.class, () -> serviceU.conectar(santuario.getId(), santuario.getId()));
    }

    @Test
    void caminoMasCorto_unSoloSalto() {
        serviceU.conectar(santuario.getId(), cementerio.getId());

        List<Ubicacion> ruta = serviceU.caminoMasCorto(santuario.getId(), cementerio.getId());
        assertEquals(2, ruta.size(), "Un solo salto debe devolver dos nodos");
        assertEquals(santuario.getId(), ruta.get(0).getId(), "El primer nodo es el origen");
        assertEquals(cementerio.getId(), ruta.get(1).getId(), "El segundo nodo es el destino");
    }

    @Test
    void caminoMasCorto_eligeRutaMasCorta() {
        Ubicacion x = serviceU.guardar(new Santuario("X", 20));
        Ubicacion y = serviceU.guardar(new Santuario("Y", 30));
        Ubicacion z = serviceU.guardar(new Santuario("Z", 40));

        // Ruta larga: A->X->Y->Z
        serviceU.conectar(santuario.getId(), x.getId());
        serviceU.conectar(x.getId(), y.getId());
        serviceU.conectar(y.getId(), z.getId());

        // Ruta directa corta: A->Z
        serviceU.conectar(santuario.getId(), z.getId());

        List<Ubicacion> ruta = serviceU.caminoMasCorto(santuario.getId(), z.getId());
        assertEquals(2, ruta.size(), "Debe elegir la ruta directa A->Z");
        assertEquals(List.of(santuario.getId(), z.getId()), ruta.stream().map(Ubicacion::getId).toList());
    }

    @Test
    void caminoMasCorto_direccionNoBidireccional() {
        serviceU.conectar(santuario.getId(), cementerio.getId());

        assertTrue(serviceU.estanConectadas(santuario.getId(), cementerio.getId()));
        assertThrows(UbicacionesNoConectadasException.class,
                () -> serviceU.caminoMasCorto(cementerio.getId(), santuario.getId()));
    }
    @Test
    void retornarMismaId(){

        Optional<Ubicacion> neo = serviceU.recuperar(santuario.getId());
        assertEquals(santuario.getId(), neo.get().getId());
        assertEquals(0, neo.get().getConexiones().size());
    }
    @Test
    void caminoMasCorto_variosSaltos_debeDevolverTodaLaCadena() {
        Ubicacion b = serviceU.guardar(new Santuario("B", 20));
        Ubicacion c = serviceU.guardar(new Santuario("C", 30));

        // A → B → C → cementerio
        serviceU.conectar(santuario.getId(), b.getId());
        serviceU.conectar(b.getId(), c.getId());
        serviceU.conectar(c.getId(), cementerio.getId());

        List<Ubicacion> ruta = serviceU.caminoMasCorto(santuario.getId(), cementerio.getId());

        assertEquals(4, ruta.size(), "Tres saltos deben devolver cuatro nodos");
        List<Long> ids = ruta.stream().map(Ubicacion::getId).toList();
        assertEquals(
                List.of(santuario.getId(), b.getId(), c.getId(), cementerio.getId()),
                ids,
                "La ruta debe seguir el orden A → B → C → D"
        );
    }
    //-------------------------------------------------------------------------------------

    @AfterEach
    void cleanup() {
        dataService.eliminarTodo();
    }
}
