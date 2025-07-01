package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.exception.BadRequest.CoordenadaFueraDeAreaException;
import ar.edu.unq.epersgeist.exception.BadRequest.CoordenadaFueraDeRangoException;
import ar.edu.unq.epersgeist.exception.Conflict.*;
import ar.edu.unq.epersgeist.exception.Conflict.RecursoNoEliminable.MediumNoEliminableException;
import ar.edu.unq.epersgeist.exception.NotFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.exception.NotFound.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.exception.NotFound.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.generador.Generador;
import ar.edu.unq.epersgeist.modelo.generador.GeneradorSecuencial;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;

import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DAOs.MediumDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.MediumMongoDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MediumServiceTest {

    @Autowired private MediumService serviceM;
    @Autowired private EspirituService serviceE;
    @Autowired private UbicacionService serviceU;

    @Autowired private DataService dataService;
    @Autowired private MediumDAOMongo mediumDAOMongo;
    @Autowired private EspirituDAOMongo espirituDAOMongo;

    private Medium medium1;
    private Medium medium2;
    private Espiritu demonio;
    private Espiritu demonCementerio;
    private Espiritu angel;
    private Ubicacion santuario;
    private Ubicacion cementerio;

    private Coordenada c1;
    private Coordenada c2;
    private Coordenada c3;
    private Coordenada c4;
    private Coordenada c5;
    private Coordenada c6;
    private Coordenada c7;
    private Coordenada c8;
    private Poligono poligonoSantuario;
    private Poligono poligonoCementerio;

    @BeforeEach
    void setUp() {
        c1 = new Coordenada(0.00, 0.00);       // Inferior izquierda
        c2 = new Coordenada(0.00, 0.18);       // Inferior derecha
        c3 = new Coordenada(0.18, 0.18);       // Superior derecha
        c4 = new Coordenada(0.18, 0.00);       // Superior izquierda

        // mismo polígono pero corrido a la derecha
        c5 = new Coordenada(0.00, 0.225);
        c6 = new Coordenada(0.00, 0.405);
        c7 = new Coordenada(0.18, 0.405);
        c8 = new Coordenada(0.18, 0.225);
        List<Coordenada> coordenadasSantuario = Arrays.asList(c1, c2, c3, c4, c1);
        poligonoSantuario = new Poligono(coordenadasSantuario);

        List<Coordenada> coordenadasCementerio = Arrays.asList(c5, c6, c7, c8, c5);
        poligonoCementerio = new Poligono(coordenadasCementerio);

        Generador.setEstrategia(new GeneradorSecuencial(0, 0, 0, 0)); // setear atk y def de espiritus creados en 0

        cementerio = new Cementerio("La Plata", 4);
        santuario  = new Santuario("Quilmes",70);
        santuario  = serviceU.guardar(santuario, poligonoSantuario);
        cementerio = serviceU.guardar(cementerio, poligonoCementerio);

        medium1         = new Medium("Pablo", 100, 50, cementerio);
        medium2         = new Medium("Fidol", 100, 50, santuario);
        demonio         = new EspirituDemoniaco("Jose", santuario);
        demonCementerio = new EspirituDemoniaco("Juan", cementerio);
        angel           = new EspirituAngelical( "kici", cementerio);
        medium1         = serviceM.guardar(medium1, c5);
        medium2         = serviceM.guardar(medium2, c1);
        demonio         = serviceE.guardar(demonio, c1);
        demonCementerio = serviceE.guardar(demonCementerio, c5);
        angel           = serviceE.guardar(angel, c5);

    }

    @Test
    void exorcizar_DosAngelesDerrotanUnDemonio_MismaUbicacionAlMoverse() {
        Generador.setEstrategia(new GeneradorSecuencial(10, 1, 10, 1));

        Espiritu angel1 = new EspirituAngelical("Ángel1", cementerio);
        Espiritu angel2 = new EspirituAngelical("Ángel2", cementerio);
        angel1.setNivelDeConexion(20); // 30 al conectarse, daño = 15
        angel2.setNivelDeConexion(30); // 40 al conectarse, daño = 20

        demonio.setNivelDeConexion(25); // 35 al conectarse
        demonio = serviceE.actualizar(demonio);
        angel1 = serviceE.guardar(angel1, c6);
        angel2 = serviceE.guardar(angel2, c6);

        medium1 = serviceE.conectar(angel1. getId(), medium1.getId());
        medium1 = serviceE.conectar(angel2. getId(), medium1.getId());
        medium2 = serviceE.conectar(demonio. getId(), medium2.getId());

        serviceU.conectar(santuario.getId(), cementerio.getId());

        serviceM.mover(medium2.getId(), c5.getLatitud(), c5.getLongitud()); // mover a cementerio

        serviceM.exorcizar(medium1.getId(), medium2.getId());


        Optional<Espiritu> demonioActualizado = serviceE.recuperar(demonio.getId());
        Optional<Espiritu> angel1Actualizado = serviceE.recuperar(angel1.getId());
        Optional<Espiritu> angel2Actualizado = serviceE.recuperar(angel2.getId());
        List<Espiritu> espiritusMedium2 = serviceM.espiritus(medium2.getId());

        assertEquals(0, demonioActualizado.get().getNivelDeConexion());
        assertFalse(demonioActualizado.get().estaConectado());
        assertTrue(espiritusMedium2.isEmpty());
        assertEquals(30, angel1Actualizado.get().getNivelDeConexion());
        assertEquals(40, angel2Actualizado.get().getNivelDeConexion());
    }
    @Test
    void testGuardarFallaPorCoordenadaNoValida() {
        Medium nuevoMedium = new Medium("Pablo", 100, 50, cementerio);

        assertThrows(CoordenadaFueraDeAreaException.class, () -> serviceM.guardar(nuevoMedium, c1) );
    }
    @Test
    void invocarFallaPorDistanciaTest(){
        Coordenada c = new Coordenada(80.1, 150.3);
        medium1 = serviceM.actualizar(medium1, c);
        assertThrows(EspirituMuyLejanoException.class,
                () -> serviceM.invocar(medium1.getId(), demonCementerio.getId()));
    }

    @Test
    void testUpdateATDeMedium(){
        String nuevoNombre = "Nuevo nombre Medium";

        medium1.setNombre(nuevoNombre);
        medium1.setUbicacion(santuario);
        serviceM.actualizar(medium1);

        Date fechaEsperada = new Date();

        Date fechaEspiritu = medium1.getUpdatedAt();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String esperadaFormateada = sdf.format(fechaEsperada);
        String obtenidaFormateada = sdf.format(fechaEspiritu);

        assertEquals(esperadaFormateada, obtenidaFormateada);
        assertEquals(medium1.getNombre(),nuevoNombre);
        assertEquals(medium1.getUbicacion().getNombre(),santuario.getNombre());

    }

    @Test
    void testCreateAtDeMedium(){
        Date fechaEsperada = new Date();

        Date fechaEspiritu = medium1.getCreatedAt();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String esperadaFormateada = sdf.format(fechaEsperada);
        String obtenidaFormateada = sdf.format(fechaEspiritu);

        assertEquals(esperadaFormateada, obtenidaFormateada);

    }

    @Test
    void recuperarMediumEliminado_devuelveOptionalVacio() {
        serviceM.eliminar(medium1.getId());

        assertTrue(serviceM.recuperar(medium1.getId()).isEmpty());
    }

    @Test
    void recuperarMediumQueNuncaExistio_devuelveOptionalVacio() {
        assertTrue(serviceM.recuperar(9999L).isEmpty());
    }

    @Test
    void recuperarMediumEliminado() {
        serviceM.eliminar(medium1.getId());
        Optional<Medium> recuperadoEliminado = dataService.recuperarEliminadoMedium(medium1.getId());
        assertTrue(recuperadoEliminado.get().isDeleted());
    }
    @Test
    void recuperarTodosLosMediumEliminados() {
        serviceM.eliminar(medium1.getId());
        List<Medium> recuperadoEliminado = dataService.recuperarTodosMediumsEliminados();
        assertEquals(1, recuperadoEliminado.size());
    }
    @Test
    void recuperarMedium_inexistente_devuelveOptionalVacio() {
        assertTrue(serviceM.recuperar(999L).isEmpty());
    }


    //casos desfavorables mover
    @Test
    void moverMedium_aCoordenadaInvalida_lanzaExcepcion(){
        assertThrows(CoordenadaFueraDeRangoException.class,
                () -> serviceM.mover(medium1.getId(), 200.0, 200.0));

        assertThrows(CoordenadaFueraDeRangoException.class,
                () -> serviceM.mover(medium1.getId(), -200.0, -200.0));

        assertThrows(CoordenadaFueraDeRangoException.class,
                () -> serviceM.mover(medium1.getId(), -130.0, 40.0));

        assertThrows(CoordenadaFueraDeRangoException.class,
                () -> serviceM.mover(medium1.getId(), 37.0, 182.0));
    }

    @Test
    void moverMedium_aCoordenadaNoIncluidaEnNingunAreaDeUbicacion_lanzaExcepcion() {
        assertThrows(UbicacionNoEncontradaException.class,
                () -> serviceM.mover(medium1.getId(), 50.0, 40.0));
    }

    @Test
    void moverMedium_aUbicacionNoConectadaConLaActual_lanzaExcepcion(){
        assertThrows(UbicacionLejanaException.class,
                () -> serviceM.mover(medium1.getId(), c2.getLatitud(), c2.getLongitud()));
    }

    @Test
    void moverMedium_aUbicacionAMasDe30Km_lanzaExcepcion(){
        serviceU.conectar(cementerio.getId(), santuario.getId());

        assertThrows(UbicacionLejanaException.class,
                () -> serviceM.mover(medium1.getId(), c4.getLatitud(), c4.getLongitud()));

    }

    @Test
    void moverMedium_inexistente_lanzaExcepcion(){
        assertThrows(MediumNoEncontradoException.class,
                () -> serviceM.mover(999L, c2.getLatitud(), c2.getLongitud()));
    }

    @Test
    void moverMedium_conEspiritus_actualizaUbicacionesYCoordenadasEnCascada() {
        serviceU.conectar(santuario.getId(), cementerio.getId());

        medium2.conectarseAEspiritu(demonio);
        serviceM.actualizar(medium2);

        serviceM.mover(medium2.getId(), c5.getLatitud(), c5.getLongitud());

        Optional<Medium> mediumActualizado = serviceM.recuperar(medium2.getId());
        Optional<Espiritu> demonioActualizado = serviceE.recuperar(demonio.getId());

        assertTrue(mediumActualizado.isPresent());
        assertTrue(demonioActualizado.isPresent());
        MediumMongoDTO mediumDTO = mediumDAOMongo.findByIdSQL(mediumActualizado.get().getId()).get();
        EspirituMongoDTO demonioDTO = espirituDAOMongo.findByIdSQL(demonioActualizado.get().getId()).get();

        assertEquals(c5.getLatitud(), mediumDTO.getPunto().getY());
        assertEquals(c5.getLongitud(), mediumDTO.getPunto().getX());

        assertEquals(c5.getLatitud(), demonioDTO.getPunto().getY());
        assertEquals(c5.getLongitud(), demonioDTO.getPunto().getX());

        assertEquals(cementerio.getId(), mediumActualizado.get().getUbicacion().getId());
        assertEquals(cementerio.getId(), demonioActualizado.get().getUbicacion().getId());
    }

    @Test
    void testInvocar() {

        Espiritu invocado = serviceM.invocar(medium1.getId(), demonio.getId());
        Medium actualizado = serviceM.recuperar(medium1.getId()).get();
        assertEquals(0, invocado.getNivelDeConexion());
        assertEquals(40, actualizado.getMana());
        assertEquals("La Plata", invocado.getUbicacion().getNombre());
    }

    @Test
    void invocar_espirituInexistente_lanzaExcepcion() {
        assertThrows(EspirituNoEncontradoException.class,
                () -> serviceM.invocar(medium1.getId(), 999L));
    }

    @Test
    void invocar_actualizaUbicacionEspirituEnDB() {
        Espiritu nuevoDemonio = new EspirituDemoniaco("NuevoDemonio", santuario);
        nuevoDemonio = serviceE.guardar(nuevoDemonio, c1);

        serviceM.invocar(medium1.getId(), nuevoDemonio.getId());

        Optional<Espiritu> demonioActualizado = serviceE.recuperar(nuevoDemonio.getId());
        assertEquals(cementerio.getId(), demonioActualizado.get().getUbicacion().getId());
    }

    @Test
    void testInvocarFallaPorqueEspirituYaEstaConectado() {
        demonio.setMediumConectado(medium1);
        serviceE.actualizar(demonio);
        assertThrows(DistanciaNoCercanaException.EspirituOcupadoException.class, () -> {
            serviceM.invocar(medium1.getId(), demonio.getId());
        });
    }

    @Test
    void testInvocarNoHaceNadaPorqueSeTieneSuficienteMana() {
        medium1.setMana(7);
        demonio.setUbicacion(cementerio);
        serviceM.actualizar(medium1);
        demonio.setUbicacion(santuario);
        serviceE.actualizar(demonio);
        Espiritu espirituRecuperado = serviceM.invocar(medium1.getId(), demonio.getId());
        assertNotEquals(medium1.getUbicacion(), espirituRecuperado.getUbicacion());
    }

    @Test
    void testCrearYRecuperarMedium() {
        Optional<Medium> recuperado = serviceM.recuperar(medium1.getId());
        assertEquals(medium1.getNombre(), recuperado.get().getNombre());
        assertEquals(medium1.getMana(), recuperado.get().getMana());
    }

    @Test
    void testRecuperarTodosLosMediums() {
        List<Medium> todos = serviceM.recuperarTodos();
        assertEquals(2, todos.size());
    }

    @Test
    void testEliminarMedium() {
        serviceM.eliminar(medium1.getId());
        List<Medium> restantes = serviceM.recuperarTodos();
        assertEquals(1, restantes.size());
    }
    @Test
    void testEliminarMediumConEspiritusLanzaException() {
        serviceE.conectar(angel.getId(), medium1.getId());

        assertThrows(MediumNoEliminableException.class, () -> serviceM.eliminar(medium1.getId()) );
    }

    @Test
    void testEliminarTodosLosMediums() {
        dataService.eliminarTodo();
        List<Medium> vacio = serviceM.recuperarTodos();
        assertTrue(vacio.isEmpty());
    }

    @Test
    void testEspiritusDeUnMedium() {
        medium1.conectarseAEspiritu(angel);
        serviceM.actualizar(medium1);

        List<Espiritu> espiritusDelMedium = serviceM.espiritus(medium1.getId());

        assertEquals(1, espiritusDelMedium.size());
        assertTrue(espiritusDelMedium.stream().anyMatch(e -> e.getId().equals(angel.getId())));
    }

    @Test
    void testNoHayEspiritusDeUnMedium() {
        List<Espiritu> espiritusDelMedium = serviceM.espiritus(medium1.getId());

        assertEquals(0, espiritusDelMedium.size());
    }

    @Test
    void testAngelesDeUnMedium() {
        medium1.conectarseAEspiritu(angel);
        medium1.conectarseAEspiritu(demonCementerio);
        serviceM.actualizar(medium1);

        List<EspirituAngelical> angelesDelMedium = serviceM.angeles(medium1.getId());

        assertEquals(1, angelesDelMedium.size());
        assertTrue(angelesDelMedium.stream().anyMatch(e -> e.getId().equals(angel.getId())));
    }

    @Test
    void testNoHayAngelesDeUnMedium() {
        List<EspirituAngelical> angelesDelMedium = serviceM.angeles(medium1.getId());

        assertEquals(0, angelesDelMedium.size());
    }

    @Test
    void testDemoniosDeUnMedium() {
        medium1 = serviceE.conectar(angel.getId(), medium1.getId());
        medium1 = serviceE.conectar(demonCementerio.getId(), medium1.getId());
        List<EspirituDemoniaco> demoniosDelMedium = serviceM.demonios(medium1.getId());

        assertEquals(1, demoniosDelMedium.size());
        assertTrue(demoniosDelMedium.stream()
                .anyMatch(e -> e.getId().equals(demonCementerio.getId())));
    }

    @Test
    void testNoHayDemoniosDeUnMedium() {
        List<EspirituDemoniaco> demoniosDelMedium = serviceM.demonios(medium1.getId());

        assertEquals(0, demoniosDelMedium.size());
    }

    @Test
    void descansar_conDemonioEnSantuario_recuperaMedium_DemonioQuedaIgual(){
        demonio.setNivelDeConexion(10);
        medium2.setMana(5);
        medium2.conectarseAEspiritu(demonio);//5*0.2=1, 10+1=11

        serviceM.actualizar(medium2);
        serviceE.actualizar(demonio);

        serviceM.descansar(medium2.getId());//100*1.5=150

        Optional<Medium> mediumRecuperado = serviceM.recuperar(medium2.getId());
        Optional<Espiritu> demonioRecuperado = serviceE.recuperar(demonio.getId());

        assertEquals(11, demonioRecuperado.get().getNivelDeConexion());
        assertEquals(100, mediumRecuperado.get().getMana());
    }

    @Test
    void descansar_conAngelEnCementerio_recuperaMedium_AngelQuedaIgual() {
        medium1.setMana(5);
        medium1.conectarseAEspiritu(angel);

        serviceM.actualizar(medium1);
        serviceE.actualizar(angel);
        serviceM.descansar(medium1.getId());

        Optional<Medium> mediumRecuperado = serviceM.recuperar(medium1.getId());
        Optional<Espiritu> angelRecuperado = serviceE.recuperar(angel.getId());

        assertEquals(1, angelRecuperado.get().getNivelDeConexion());
        assertEquals(7, mediumRecuperado.get().getMana());
    }

    @Test
    void descansarSinEspiritus(){
        medium1.setMana(5);
        serviceM.actualizar(medium1);
        serviceM.descansar(medium1.getId());
        Optional<Medium> mediumRecuperado = serviceM.recuperar(medium1.getId());
        assertEquals(7, mediumRecuperado.get().getMana());
    }
    @Test
    void descansarPeroElMagoLlegaAlLimiteDeMana(){
        medium1.setMana(98);
        medium1.conectarseAEspiritu(angel);
        serviceM.actualizar(medium1);
        serviceM.descansar(medium1.getId());
        Optional<Medium> mediumRecuperado = serviceM.recuperar(medium1.getId());
        assertEquals(100, mediumRecuperado.get().getMana());
    }

    @Test
    void descansar_conDemonio_recuperanConexiones() {
        demonio.setNivelDeConexion(10);
        demonio.setUbicacion(cementerio);
        medium1.conectarseAEspiritu(demonio);//50*0.2=10, 10+10=20

        serviceE.actualizar(demonio);
        serviceM.actualizar(medium1);
        serviceM.descansar(medium1.getId()); //52, 50

        Optional<Medium> mediumRecuperado = serviceM.recuperar(medium1.getId());
        Optional<Espiritu> demonioActualizado = serviceE.recuperar(demonio.getId());

        assertEquals(52, mediumRecuperado.get().getMana());
        assertEquals(24, demonioActualizado.get().getNivelDeConexion());
    }

    @Test
    void descansar_conAngel_recuperanConexiones() {
        angel.setNivelDeConexion(10);
        angel.setUbicacion(santuario);
        medium2.conectarseAEspiritu(angel); // 50*0.2=10, 10+10=20

        serviceE.actualizar(angel);
        serviceM.actualizar(medium2);

        serviceM.descansar(medium2.getId());

        Optional<Medium> mediumRecuperado = serviceM.recuperar(medium2.getId());
        Optional<Espiritu> angelActualizado = serviceE.recuperar(angel.getId());

        assertEquals(100, mediumRecuperado.get().getMana());
        assertEquals(90, angelActualizado.get().getNivelDeConexion());
    }

    @Test
    void descansar_conVariosEspiritus() {
        angel.setNivelDeConexion(10);
        demonio.setNivelDeConexion(30);
        angel.setUbicacion(santuario);
        medium2.conectarseAEspiritu(angel); // 50*0.2=10, 10+10=20
        medium2.conectarseAEspiritu(demonio);

        serviceE.actualizar(angel);
        serviceE.actualizar(demonio);
        serviceM.actualizar(medium2);

        serviceM.descansar(medium2.getId());

        Optional<Medium> mediumRecuperado = serviceM.recuperar(medium2.getId());
        Optional<Espiritu> angelActualizado = serviceE.recuperar(angel.getId());
        Optional<Espiritu> demonioActualizado = serviceE.recuperar(demonio.getId());

        assertEquals(100, mediumRecuperado.get().getMana());
        assertEquals(90, angelActualizado.get().getNivelDeConexion());
        assertEquals(40, demonioActualizado.get().getNivelDeConexion());
    }

    @Test
    void noSePuedeExorcizar_diferenteUbicacion() {
        medium1.conectarseAEspiritu(angel);
        medium2.conectarseAEspiritu(demonio);
        serviceM.actualizar(medium1);
        serviceM.actualizar(medium2);

        assertThrows(
                ExorcizarNoPermitidoNoEsMismaUbicacion.class,
                () -> serviceM.exorcizar(medium1.getId(), medium2.getId())
        );
    }

    @Test
    void exorcizarA_AtaqueExitoso_DemonioDerrotado_MismaUbicacionAlMoverse() {
        Generador.setEstrategia(new GeneradorSecuencial(10, 1)); // 10 + nivel > 1

        angel.setNivelDeConexion(20);
        demonio.setNivelDeConexion(5);
        demonio = serviceE.actualizar(demonio);
        angel = serviceE.actualizar(angel);

        medium1 = serviceE.conectar(angel.getId(), medium1.getId()); // angel: 20 + 10 = 30 (daño = 15)
        medium2 = serviceE.conectar(demonio.getId(), medium2.getId()); // demonio: 5 + 10 = 15

        serviceU.conectar(santuario.getId(), cementerio.getId());
        serviceU.conectar(cementerio.getId(), santuario.getId());

        serviceM.mover(medium2.getId(), c5.getLatitud(), c5.getLongitud()); // mover a cementerio

        serviceM.exorcizar(medium1.getId(), medium2.getId());

        Optional<Espiritu> angelActualizado = serviceE.recuperar(angel.getId());
        Optional<Espiritu> demonioActualizado = serviceE.recuperar(demonio.getId());
        List<Espiritu> espiritusMedium2 = serviceM.espiritus(medium2.getId());

        assertEquals(0, demonioActualizado.get().getNivelDeConexion());
        assertFalse(demonioActualizado.get().estaConectado());
        assertTrue(espiritusMedium2.isEmpty());
        assertEquals(30, angelActualizado.get().getNivelDeConexion());
    }



    @Test
    void exorcizar_ExorcistaSinAngeles_LanzaExcepcion_MismaUbicacionAlMoverse() {

        serviceE.conectar(demonio.getId(), medium2.getId());
        serviceU.conectar(cementerio.getId(), santuario.getId());
        serviceM.mover(medium1.getId(), c1.getLatitud(), c2.getLongitud()); // mover a santuario
        assertThrows(ExorcistaSinAngelesException.class, () -> {
            serviceM.exorcizar(medium1.getId(), medium2.getId());
        });
    }

    @Test
    void exorcizar_DemonioYaDesconectado_NoHaceNada() {
        demonio.setNivelDeConexion(0);
        demonio.setMediumConectado(null);
        serviceE.actualizar(demonio);

        serviceE.conectar(angel.getId(), medium1.getId());

        serviceU.conectar(santuario.getId(), cementerio.getId());
        serviceM.mover(medium2.getId(), c5.getLatitud(), c5.getLongitud()); // mover a cementerio
        assertDoesNotThrow(() -> {
            serviceM.exorcizar(medium1.getId(), medium2.getId());
        });
    }
    @Test
    void exorcizar_enDistintaUbicacion_lanzaError(){
        serviceE.conectar(angel.getId(), medium1.getId());
        serviceE.conectar(demonio.getId(), medium2.getId());
        assertThrows(ExorcizarNoPermitidoNoEsMismaUbicacion.class, () -> {
            serviceM.exorcizar(medium1.getId(), medium2.getId());
        });
    }

    @Test
    void exorcizar_AtaqueFallido_AngelPierdeEnergia_MismaUbicacionAlMoverse() {
        Generador.setEstrategia(new GeneradorSecuencial(1, 100));

        angel.setNivelDeConexion(20);
        demonio.setNivelDeConexion(10);
        angel = serviceE.actualizar(angel);
        demonio = serviceE.actualizar(demonio);

        medium1 = serviceE.conectar(angel.getId(), medium1.getId());
        medium2 = serviceE.conectar(demonio.getId(), medium2.getId());

        Optional<Espiritu> angelRecuperado = serviceE.recuperar(angel.getId());
        Optional<Espiritu> demonioRecuperado = serviceE.recuperar(demonio.getId());

        serviceU.conectar(santuario.getId(), cementerio.getId());
        serviceM.mover(medium2.getId(), c5.getLatitud(), c5.getLongitud()); // mover a cementerio

        assertEquals(30, angelRecuperado.get().getNivelDeConexion());//20+10=30
        assertEquals(20, demonioRecuperado.get().getNivelDeConexion());//10+10=20

        serviceM.exorcizar(medium1.getId(), medium2.getId());

        assertTrue(medium1.getEspiritus().contains(angelRecuperado.get()),
                "El ángel debería estar conectado al medium exorcista");

        assertTrue(medium2.getEspiritus().contains(demonioRecuperado.get()),
                "El demonio debería estar conectado al medium exorcizado");


        Optional<Espiritu> demonioActualizado = serviceE.recuperar(demonioRecuperado.get().getId());
        assertEquals(20, demonioActualizado.get().getNivelDeConexion(),
                "El demonio no debería haber sido afectado en un ataque fallido");

        Optional<Espiritu> angelActualizado = serviceE.recuperar(angelRecuperado.get().getId());

        assertEquals(25, angelActualizado.get().getNivelDeConexion(),
                "El ángel debería haber perdido 5 puntos de conexión al fallar el ataque");
    }

   @AfterEach
   void cleanUp() {
        dataService.eliminarTodo();
   }
}
