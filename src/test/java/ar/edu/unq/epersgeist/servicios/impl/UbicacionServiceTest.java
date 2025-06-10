package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import ar.edu.unq.epersgeist.exception.MismaUbicacionException;
import ar.edu.unq.epersgeist.exception.UbicacionNoEliminableException;
import ar.edu.unq.epersgeist.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.exception.UbicacionesNoConectadasException;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;

import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.MediumRepository;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.UbicacionRepository;
import ar.edu.unq.epersgeist.servicios.interfaces.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    private Ubicacion ubicacion1;
    private Ubicacion ubicacion2;
    private Ubicacion ubicacion3;
    private Ubicacion ubicacion4;
    private Espiritu angel;
    private Espiritu demonio;

    private Coordenada c1;
    private Coordenada c4;
    private Coordenada c3;
    private Coordenada c2;
    private Poligono poligono;

    @BeforeEach
    void setUp() {
        c1 = new Coordenada(1.0,1.0);
        c2 = new Coordenada(2.0,2.0);
        c3 = new Coordenada(3.0,3.0);
        c4 = new Coordenada(-1.0,-1.0);
        List<Coordenada> coordenadas = Arrays.asList(c1, c2, c3, c4, c1);
        poligono = new Poligono(coordenadas);

        santuario = new Santuario("Quilmes", 70);
        cementerio = new Cementerio("Bernal", 60);


        angel = new EspirituAngelical("damian", santuario);
        demonio = new EspirituDemoniaco("Roberto", santuario);

        medium = new Medium("roberto", 200, 150, santuario);
        medium2 = new Medium("roberto", 200, 150, santuario);
        medium3 = new Medium("roberto", 200, 150, santuario);
        santuario = serviceU.guardar(santuario, poligono);
        cementerio = serviceU.guardar(cementerio, poligono);
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
        santuario = serviceU.guardar(santuario, poligono);

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
        serviceE.guardar(angel, c1);
        serviceE.guardar(demonio, c1);

        List<Espiritu> espiritusEn = serviceU.espiritusEn(santuario.getId());
        assertEquals(2, espiritusEn.size());
    }

    @Test
    void espiritusEnUnaUbicacionExistenteSinEliminados() {
        angel = serviceE.guardar(angel, c1);
        serviceE.guardar(demonio, c1);
        serviceE.eliminar(angel.getId());
        List<Espiritu> espiritusEn = serviceU.espiritusEn(santuario.getId());
        assertEquals(1, espiritusEn.size());
    }

    @Test
    void espiritusEnUnaUbicacionInexistente() {
        serviceE.guardar(angel, c1);
        serviceE.guardar(demonio, c1);

        List<Espiritu> espiritusEn = serviceU.espiritusEn(cementerio.getId());
        assertEquals(0, espiritusEn.size());
    }

    @Test
    void mediumsSinEspiritusEnUbicacion() {
        medium = serviceM.guardar(medium, c1);
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(santuario.getId());
        assertEquals(1, mediums.size());
        assertEquals(medium.getId(), mediums.getFirst().getId());
    }

    @Test
    void noHayMediumsEnBernal() {
        serviceM.guardar(medium, c1);
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(cementerio.getId());
        assertEquals(0, mediums.size());
    }

    @Test
    void hayMediumsPeroTienenEspiritusDespuesDeConectarseEnQuilmes() {
        angel = serviceE.guardar(angel, c1);
        medium = serviceM.guardar(medium, c1);
        serviceE.conectar(angel.getId(), medium.getId());
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(santuario.getId());
        assertEquals(0, mediums.size());
    }

    @Test
    void mediumEliminadoEnSantuario() {
        medium = serviceM.guardar(medium, c1);
        serviceM.eliminar(medium.getId());
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(santuario.getId());
        assertEquals(0, mediums.size());
    }

    @Test
    void hayMediumsConUnMediumEliminadoEnSantuario() {
        medium = serviceM.guardar(medium, c1);
        serviceM.eliminar(medium.getId());
        serviceM.guardar(medium2, c1);
        serviceM.guardar(medium3, c1);
        List<Medium> mediums = serviceU.mediumsSinEspiritusEn(santuario.getId());
        assertEquals(2, mediums.size());
    }

    @Test
    void noHayMediumsSinEspiritusEnUbicacionInexistente() {
        serviceM.guardar(medium, c1);
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
        serviceU.guardar(q.get(), poligono);
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

        serviceE.guardar(angel, c1);

        assertThrows(UbicacionNoEliminableException.class, () -> serviceU.eliminar(santuario.getId()));
    }

    @Test
    void eliminarUbicacionLanzaExceptionPorQueExisteUnMediumEnEsaUbicacion() {

        serviceM.guardar(medium, c1);

        assertThrows(UbicacionNoEliminableException.class, () -> serviceU.eliminar(santuario.getId()));
    }

    //-----NEO---------------------------------------------------------------------------
    @Test
    void ubicacionesSobrecargadasCasoFavorable(){
        List<Ubicacion> ubicaciones = serviceU.ubicacionesSobrecargadas(50);
        assertTrue(ubicaciones.contains(santuario));
        assertTrue(ubicaciones.contains(cementerio));
        assertEquals(2, ubicaciones.size());
    }

    @Test
    void ubicacionesSobrecargadasCasoDesfavorable(){
        List<Ubicacion> ubicaciones = serviceU.ubicacionesSobrecargadas(70);
        assertTrue(ubicaciones.isEmpty());
    }


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
        Ubicacion x = serviceU.guardar(new Santuario("X", 20), poligono);
        Ubicacion y = serviceU.guardar(new Santuario("Y", 30), poligono);
        Ubicacion z = serviceU.guardar(new Santuario("Z", 40), poligono);

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
        Ubicacion b = serviceU.guardar(new Santuario("B", 20), poligono);
        Ubicacion c = serviceU.guardar(new Santuario("C", 30), poligono);

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
                "La ruta debe seguir el orden A → B → C → cementerio"
        );
    }

    @Test
    void caminoMasCorto_OrigenEqualsDestino_DeberiaRetornarSoloEseNodo() {
        Long id = santuario.getId();

        List<Ubicacion> resultado = serviceU.caminoMasCorto(id, id);

        assertEquals(1, resultado.size(), "Cuando origen == destino, la ruta debe tener un único elemento.");

        Ubicacion obtenido = resultado.get(0);
        assertNotNull(obtenido, "El elemento no debe ser null.");
        assertEquals(id, obtenido.getId(), "El único elemento devuelto debe tener el mismo ID que el origen.");

        assertEquals("Quilmes", obtenido.getNombre());
        assertEquals(70, obtenido.getFlujoDeEnergia());
        assertEquals(TipoUbicacion.SANTUARIO, obtenido.getTipo());
    }

    @Test
    void recuperarConexiones_casoFeliz_debeDevolverVecinosDirectos() {

        serviceU.conectar(santuario.getId(), cementerio.getId());

        List<Ubicacion> vecinosDeA = serviceU.recuperarConexiones(santuario.getId());

        assertEquals(1, vecinosDeA.size(), "Después de conectar, debe haber exactamente 1 vecino");
        assertEquals(cementerio.getId(), vecinosDeA.get(0).getId(),
                "El único vecino debe ser el cementerio al que conectamos");

        List<Ubicacion> vecinosDeB = serviceU.recuperarConexiones(cementerio.getId());
        assertTrue(vecinosDeB.isEmpty(), "Sin conexión inversa, B no debería tener vecinos directos");
    }

    @Test
    void caminoMasCorto_idOrigenNoExiste_debeLanzarUbicacionNoEncontradaException() {
        long idInexistente = 9999L;
        assertTrue(serviceU.recuperar(idInexistente).isEmpty());

        assertThrows(UbicacionNoEncontradaException.class,
                () -> serviceU.caminoMasCorto(idInexistente, santuario.getId()),
                "Si el origen no existe, debe lanzarse UbicacionNoEncontradaException");
    }

    @Test
    void caminoMasCorto_idDestinoNoExiste_debeLanzarUbicacionNoEncontradaException() {
        long idInexistente = 8888L;
        assertTrue(serviceU.recuperar(idInexistente).isEmpty());

        assertThrows(UbicacionNoEncontradaException.class,
                () -> serviceU.caminoMasCorto(santuario.getId(), idInexistente),
                "Si el destino no existe, debe lanzarse UbicacionNoEncontradaException");
    }

    @Test
    void conectar_multiplesVeces_conUnaMISMAArista_noDebieraFallarNiDuplicar() {
        serviceU.conectar(santuario.getId(), cementerio.getId());
        serviceU.conectar(santuario.getId(), cementerio.getId()); // segunda invocación

        List<Ubicacion> vecinos = serviceU.recuperarConexiones(santuario.getId());

        assertEquals(1, vecinos.size(), "La conexión A→B solo debe aparecer una vez, incluso si llamamos conectar dos veces.");
        assertEquals(cementerio.getId(), vecinos.get(0).getId());
    }

    @Test
    void recuperarConexiones_multiplesVecinos_debeDevolverTodosLosDestinos() {
        Ubicacion b = serviceU.guardar(new Santuario("B", 20), poligono);
        Ubicacion c = serviceU.guardar(new Santuario("C", 30), poligono);

        serviceU.conectar(santuario.getId(), b.getId());
        serviceU.conectar(santuario.getId(), c.getId());

        List<Ubicacion> vecinos = serviceU.recuperarConexiones(santuario.getId());
        List<Long> ids = vecinos.stream().map(Ubicacion::getId).toList();

        assertTrue(ids.containsAll(List.of(b.getId(), c.getId())),
                "Debe devolver todos los destinos de las conexiones salientes");
        assertEquals(2, vecinos.size());
    }

    @Test
    void conectar_conIdOrigenNoExiste_debeLanzarUbicacionNoEncontradaException() {
        long inexistente = 9999L;
        assertThrows(
                UbicacionNoEncontradaException.class,
                () -> serviceU.conectar(inexistente, cementerio.getId()),
                "Conectar con origen inexistente debe lanzar UbicacionNoEncontradaException"
        );
    }

    @Test
    void conectar_conIdDestinoNoExiste_debeLanzarUbicacionNoEncontradaException() {
        long inexistente = 8888L;
        assertThrows(
                UbicacionNoEncontradaException.class,
                () -> serviceU.conectar(santuario.getId(), inexistente),
                "Conectar con destino inexistente debe lanzar UbicacionNoEncontradaException"
        );
    }

    @Test
    void closeness() {
        ubicacion1 = new Cementerio("U1", 10);
        ubicacion2 = new Cementerio("U2", 10);
        ubicacion3 = new Cementerio("U3", 10);
        ubicacion4 = new Cementerio("U4", 10);
        ubicacion1 = serviceU.guardar(ubicacion1, poligono);
        ubicacion2 = serviceU.guardar(ubicacion2, poligono);
        ubicacion3 = serviceU.guardar(ubicacion3, poligono);
        ubicacion4 = serviceU.guardar(ubicacion4, poligono);
        serviceU.conectar(ubicacion1.getId(),ubicacion2.getId());
        serviceU.conectar(ubicacion2.getId(),ubicacion1.getId());
        serviceU.conectar(ubicacion2.getId(),ubicacion3.getId());
        serviceU.conectar(ubicacion3.getId(),ubicacion4.getId());
        serviceU.conectar(ubicacion4.getId(),ubicacion2.getId());


        /*        4     S    C
        *         | ↖
        *         ↓  3
        *   1 ↔ 2 ↗
        */

        List<Long> ids = List.of(ubicacion1.getId(),ubicacion2.getId(),ubicacion3.getId(),ubicacion4.getId(), santuario.getId(), cementerio.getId());
        List<Double> closenessEsperados = List.of(
                1.0 / 26,
                1.0 / 24,
                1.0 / 26,
                1.0 / 25,
                1.0 / 50,
                1.0 / 50
        );// tener en cuenta que santuario y cementerio están desconectados de los nodos ubicacionN (se suma 10 si no hay camino)

        List<ClosenessResult> resultados = serviceU.closenessOf(ids);

        // Reordenar los resultados para que coincidan con el orden de los IDs
        Map<Long, Double> mapaResultados = resultados.stream()
                .collect(Collectors.toMap(r -> r.ubicacion().getId(), ClosenessResult::closeness));

        for (int i = 0; i < ids.size(); i++) {
            long id = ids.get(i);
            double esperado = closenessEsperados.get(i);
            double obtenido = mapaResultados.getOrDefault(id, -1.0);
            assertEquals(esperado, obtenido, 0.001, "Error en el índice " + i + ", id=" + id);
        }
    }

    @Test
    void closenessDeUbicacionesNoExistentesLanzaExcepcion(){
        assertTrue((serviceU.closenessOf(List.of(433L, 231L)).isEmpty()));
    }


    @Test
    void degreeSinConexiones_debeSerCeroParaAmbas() {
        List<DegreeResult> resultados = serviceU.degreeOf(List.of(santuario.getId(), cementerio.getId()));

        Map<Long, Long> grado = resultados.stream()
                .collect(Collectors.toMap(
                        dr -> dr.ubicacion().getId(),
                        DegreeResult::degree
                ));

        assertEquals(0, grado.get(santuario.getId()), "Quilmes sin conexiones debe tener degree 0");
        assertEquals(0, grado.get(cementerio.getId()), "Bernal sin conexiones debe tener degree 0");
    }

    @Test
    void degreeConConexionUnidireccional_debeSerUnoParaAmbos() {
        serviceU.conectar(santuario.getId(), cementerio.getId());

        List<DegreeResult> resultados = serviceU.degreeOf(List.of(santuario.getId(), cementerio.getId()));

        Map<Long, Long> grado = resultados.stream()
                .collect(Collectors.toMap(
                        dr -> dr.ubicacion().getId(),
                        DegreeResult::degree
                ));

        assertEquals(1, grado.get(santuario.getId()),   "Quilmes con una salida debe tener degree 1");
        assertEquals(1, grado.get(cementerio.getId()), "Bernal con una entrada debe tener degree 1");
    }

    @Test
    void degreeConConexionBidireccional_debeSerDosParaAmbos() {
        serviceU.conectar(santuario.getId(), cementerio.getId());
        serviceU.conectar(cementerio.getId(), santuario.getId());

        List<DegreeResult> resultados = serviceU.degreeOf(List.of(santuario.getId(), cementerio.getId()));

        Map<Long, Long> grado = resultados.stream()
                .collect(Collectors.toMap(
                        dr -> dr.ubicacion().getId(),
                        DegreeResult::degree
                ));

        assertEquals(2, grado.get(santuario.getId()),   "Quilmes con entrada y salida debe tener degree 2");
        assertEquals(2, grado.get(cementerio.getId()), "Bernal con entrada y salida debe tener degree 2");
    }

    @Test
    void degreeDeUbicacionesNoExistentesEsListaVacia(){
        assertTrue((serviceU.degreeOf(List.of(333L, 444L)).isEmpty()));
    }

    @Test
    void crearYConectarNNodos() {
        int cantidadDeNodos = 50;
        int cantidadDeConexionesPorNodo = 3;

        List<Ubicacion> nodos = new ArrayList<>();
        for (int i = 0; i < cantidadDeNodos; i++) {
            nodos.add(serviceU.guardar(new Santuario("Nodo " + i, 10), poligono));
        }

        Random random = new Random();
        Set<String> conexionesHechas = new HashSet<>();

        for (Ubicacion nodo : nodos) {
            Set<Long> conectados = new HashSet<>();
            while (conectados.size() < cantidadDeConexionesPorNodo) {
                Ubicacion destino = nodos.get(random.nextInt(nodos.size()));
                if (!nodo.getId().equals(destino.getId()) && !conectados.contains(destino.getId())) {
                    String claveConexion = nodo.getId() + "-" + destino.getId();

                    if (!conexionesHechas.contains(claveConexion)) {
                        serviceU.conectar(nodo.getId(), destino.getId());
                        conexionesHechas.add(claveConexion);
                        conectados.add(destino.getId());
                    }
                }
            }
        }
    }

    @Test
    void cleanup() { // esto esta solo para borrar rapidamente
        dataService.eliminarTodo();
    }

    //-------------------------------------------------------------------------------------

//    @AfterEach
//    void cleanup() {
//        dataService.eliminarTodo();
//    }

}