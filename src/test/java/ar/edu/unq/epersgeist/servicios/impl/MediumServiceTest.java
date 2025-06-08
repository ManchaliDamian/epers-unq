package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.exception.*;
import ar.edu.unq.epersgeist.modelo.generador.Generador;
import ar.edu.unq.epersgeist.modelo.generador.GeneradorSecuencial;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;

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

    @Autowired private MediumRepository mediumRepository;
    @Autowired private EspirituRepository espirituRepository;
    @Autowired private UbicacionRepository ubicacionRepository;
    @Autowired private DataService dataService;

    private Medium medium1;
    private Medium medium2;
    private Espiritu demonio;
    private Espiritu demonCementerio;
    private Espiritu angel;
    private Ubicacion santuario;
    private Ubicacion cementerio;

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

        Generador.setEstrategia(new GeneradorSecuencial(50));

        cementerio = new Cementerio("La Plata", 4);
        santuario = new Santuario("Quilmes",70);
        santuario = serviceU.guardar(santuario, poligono);
        cementerio =serviceU.guardar(cementerio, poligono);

        medium1 = new Medium("Pablo", 100, 50, cementerio, c1);
        medium2 = new Medium("Fidol", 100, 50, santuario, c1);
        demonio = new EspirituDemoniaco("Jose", santuario, c1);
        demonCementerio = new EspirituDemoniaco("Juan", cementerio, c1);
        angel = new EspirituAngelical( "kici", cementerio, c1);
        medium1 = serviceM.guardar(medium1);
        medium2 = serviceM.guardar(medium2);
        demonio = serviceE.guardar(demonio);
        demonCementerio = serviceE.guardar(demonCementerio);
        angel = serviceE.guardar(angel);

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
        serviceM.guardar(medium1);

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

    @Test
    void moverMedium_aUbicacionInexistente_lanzaExcepcion() {
        assertThrows(UbicacionNoEncontradaException.class,
                () -> serviceM.mover(medium1.getId(), 999L));
    }

    @Test
    void moverMedium_conEspiritus_actualizaUbicacionesEnCascada() {
        serviceU.conectar(cementerio.getId(), santuario.getId());

        medium1.conectarseAEspiritu(angel);
        serviceM.guardar(medium1);

        serviceM.mover(medium1.getId(), santuario.getId());

        Optional<Medium> mediumActualizado = serviceM.recuperar(medium1.getId());
        Optional<Espiritu> angelActualizado = serviceE.recuperar(angel.getId());

        assertEquals(santuario.getId(), mediumActualizado.get().getUbicacion().getId());
        assertEquals(santuario.getId(), angelActualizado.get().getUbicacion().getId());
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
        Espiritu nuevoDemonio = new EspirituDemoniaco("NuevoDemonio", santuario, c1);
        nuevoDemonio = serviceE.guardar(nuevoDemonio);

        serviceM.invocar(medium1.getId(), nuevoDemonio.getId());

        Optional<Espiritu> demonioActualizado = serviceE.recuperar(nuevoDemonio.getId());
        assertEquals(cementerio.getId(), demonioActualizado.get().getUbicacion().getId());
    }

    @Test
    void testInvocarFallaPorqueEspirituYaEstaConectado() {
        demonio.setMediumConectado(medium1);
        serviceE.guardar(demonio);
        assertThrows(EspirituOcupadoException.class, () -> {
            serviceM.invocar(medium1.getId(), demonio.getId());
        });
    }
    @Test
    void testInvocarNoHaceNadaPorqueSeTieneSuficienteMana() {
        medium1.setMana(7);
        demonio.setUbicacion(cementerio);
        serviceM.guardar(medium1);
        demonio.setUbicacion(santuario);
        serviceE.guardar(demonio);
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
        serviceM.guardar(medium1);
        serviceM.guardar(medium2);
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
        serviceM.guardar(medium1);

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
        serviceM.guardar(medium1);

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
        assertTrue(demoniosDelMedium.stream().anyMatch(e -> e.getId().equals(demonCementerio.getId())));
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

        serviceM.guardar(medium2);
        serviceE.guardar(demonio);

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

        serviceM.guardar(medium1);
        serviceE.guardar(angel);
        serviceM.descansar(medium1.getId());

        Optional<Medium> mediumRecuperado = serviceM.recuperar(medium1.getId());
        Optional<Espiritu> angelRecuperado = serviceE.recuperar(angel.getId());

        assertEquals(1, angelRecuperado.get().getNivelDeConexion());
        assertEquals(7, mediumRecuperado.get().getMana());
    }

    @Test
    void descansarSinEspiritus(){
        medium1.setMana(5);
        serviceM.guardar(medium1);
        serviceM.descansar(medium1.getId());
        Optional<Medium> mediumRecuperado = serviceM.recuperar(medium1.getId());
        assertEquals(7, mediumRecuperado.get().getMana());
    }
    @Test
    void descansarPeroElMagoLlegaAlLimiteDeMana(){
        medium1.setMana(98);
        medium1.conectarseAEspiritu(angel);
        serviceM.guardar(medium1);
        serviceM.descansar(medium1.getId());
        Optional<Medium> mediumRecuperado = serviceM.recuperar(medium1.getId());
        assertEquals(100, mediumRecuperado.get().getMana());
    }

    @Test
    void descansar_conDemonio_recuperanConexiones() {
        demonio.setNivelDeConexion(10);
        demonio.setUbicacion(cementerio);
        medium1.conectarseAEspiritu(demonio);//50*0.2=10, 10+10=20

        serviceE.guardar(demonio);
        serviceM.guardar(medium1);
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

        serviceE.guardar(angel);
        serviceM.guardar(medium2);

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

        serviceE.guardar(angel);
        serviceE.guardar(demonio);
        serviceM.guardar(medium2);

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
        serviceM.guardar(medium1);
        serviceM.guardar(medium2);

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
        demonio = serviceE.guardar(demonio);
        angel = serviceE.guardar(angel);

        medium1 = serviceE.conectar(angel.getId(), medium1.getId()); // angel: 20 + 10 = 30 (daño = 15)
        medium2 = serviceE.conectar(demonio.getId(), medium2.getId()); // demonio: 5 + 10 = 15

        serviceU.conectar(santuario.getId(), cementerio.getId());
        serviceU.conectar(cementerio.getId(), santuario.getId());

        serviceM.mover(medium2.getId(), cementerio.getId());

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
    void exorcizar_DosAngelesDerrotanUnDemonio_MismaUbicacionAlMoverse() {
        Generador.setEstrategia(new GeneradorSecuencial(10, 1, 10, 1));

        Espiritu angel1 = new EspirituAngelical("Ángel1", cementerio, c1);
        Espiritu angel2 = new EspirituAngelical("Ángel2", cementerio, c1);
        angel1.setNivelDeConexion(20); // 30 al conectarse, daño = 15
        angel2.setNivelDeConexion(30); // 40 al conectarse, daño = 20

        demonio.setNivelDeConexion(25); // 35 al conectarse
        demonio = serviceE.guardar(demonio);
        angel1 = serviceE.guardar(angel1);
        angel2 = serviceE.guardar(angel2);

        medium1 = serviceE.conectar(angel1. getId(), medium1.getId());
        medium1 = serviceE.conectar(angel2. getId(), medium1.getId());
        medium2 = serviceE.conectar(demonio. getId(), medium2.getId());

        serviceU.conectar(santuario.getId(), cementerio.getId());

        serviceM.mover(medium2.getId(), cementerio.getId());

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
    void exorcizar_ExorcistaSinAngeles_LanzaExcepcion_MismaUbicacionAlMoverse() {

        serviceE.conectar(demonio.getId(), medium2.getId());
        serviceU.conectar(cementerio.getId(), santuario.getId());
        serviceM.mover(medium1.getId(), santuario.getId());
        assertThrows(ExorcistaSinAngelesException.class, () -> {
            serviceM.exorcizar(medium1.getId(), medium2.getId());
        });
    }

    @Test
    void exorcizar_DemonioYaDesconectado_NoHaceNada() {
        demonio.setNivelDeConexion(0);
        demonio.setMediumConectado(null);
        serviceE.guardar(demonio);

        serviceE.conectar(angel.getId(), medium1.getId());

        serviceU.conectar(santuario.getId(), cementerio.getId());
        serviceM.mover(medium2.getId(), cementerio.getId());
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
        serviceM.mover(medium2.getId(), cementerio.getId());

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
