package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.exception.*;
import ar.edu.unq.epersgeist.modelo.generador.Generador;
import ar.edu.unq.epersgeist.modelo.generador.GeneradorSecuencial;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DAOs.*;

import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
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

    @Autowired private MediumDAO mediumDAO;

    @Autowired private EspirituDAO espirituDAO;
    @Autowired private UbicacionDAO ubicacionDAO;

    private Medium medium1;
    private Medium medium2;
    private Espiritu demonio;
    private Espiritu demonCementerio;
    private Espiritu angel;
    private Ubicacion santuario;
    private Ubicacion cementerio;

    private DataService dataService;
    @BeforeEach
    void setUp() {
        dataService = new DataServiceImpl(ubicacionDAO,mediumDAO, espirituDAO);

        Generador.setEstrategia(new GeneradorSecuencial(50));

        cementerio = new Cementerio("La Plata", 4);
        santuario = new Santuario("Quilmes",70);
        serviceU.guardar(santuario);
        serviceU.guardar(cementerio);

        medium1 = new Medium("Pablo", 100, 50, cementerio);
        medium2 = new Medium("Fidol", 100, 50, santuario);
        demonio = new EspirituDemoniaco("Jose", santuario);
        demonCementerio = new EspirituDemoniaco("Juan", cementerio);
        angel = new EspirituAngelical( "kici", cementerio);
        serviceM.guardar(medium1);
        serviceM.guardar(medium2);
        serviceE.guardar(demonio);
        serviceE.guardar(demonCementerio);
        serviceE.guardar(angel);

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
        medium1.conectarseAEspiritu(angel);
        serviceM.guardar(medium1);

        serviceM.mover(medium1.getId(), santuario.getId());

        Optional<Medium> mediumActualizado = serviceM.recuperar(medium1.getId());
        Optional<Espiritu> angelActualizado = serviceE.recuperar(angel.getId());

        assertEquals(santuario, mediumActualizado.get().getUbicacion());
        assertEquals(santuario, angelActualizado.get().getUbicacion());
    }

    @Test
    void testInvocar() {

        Espiritu invocado = serviceM.invocar(medium1.getId(), demonio.getId());
        Medium actualizado = mediumDAO.findById(medium1.getId()).get();
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
        serviceE.guardar(nuevoDemonio);

        serviceM.invocar(medium1.getId(), nuevoDemonio.getId());

        Optional<Espiritu> demonioActualizado = serviceE.recuperar(nuevoDemonio.getId());
        assertEquals(cementerio, demonioActualizado.get().getUbicacion());
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
        medium1.conectarseAEspiritu(angel);
        medium1.conectarseAEspiritu(demonCementerio);
        serviceM.guardar(medium1);

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

        conectarEspirituAMedium(medium1, angel); // angel: 20 + 10 = 30 (daño = 15)
        conectarEspirituAMedium(medium2, demonio); // demonio: 5 + 10 = 15

        serviceM.mover(medium2.getId(), cementerio.getId());

        serviceM.exorcizar(medium1.getId(), medium2.getId());

        Optional<Espiritu> angelActualizado = serviceE.recuperar(angel.getId());
        Optional<Espiritu> demonioActualizado = serviceE.recuperar(demonio.getId());
        List<Espiritu> espiritusMedium2 = serviceM.espiritus(medium2.getId());

        assertFalse(demonioActualizado.get().estaConectado());
        assertEquals(0, demonioActualizado.get().getNivelDeConexion());
        assertTrue(espiritusMedium2.isEmpty());
        assertEquals(30, angelActualizado.get().getNivelDeConexion());
    }

    @Test
    void exorcizar_DosAngelesDerrotanUnDemonio_MismaUbicacionAlMoverse() {
        Generador.setEstrategia(new GeneradorSecuencial(10, 1, 10, 1));

        EspirituAngelical angel1 = new EspirituAngelical("Ángel1", cementerio);
        EspirituAngelical angel2 = new EspirituAngelical("Ángel2", cementerio);
        angel1.setNivelDeConexion(20); // 30 al conectarse, daño = 15
        angel2.setNivelDeConexion(30); // 40 al conectarse, daño = 20

        demonio.setNivelDeConexion(25); // 35 al conectarse

        conectarEspirituAMedium(medium1, angel1);
        conectarEspirituAMedium(medium1, angel2);
        conectarEspirituAMedium(medium2, demonio);

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

        conectarEspirituAMedium(medium2, demonio);
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

        conectarEspirituAMedium(medium1, angel);
        serviceM.mover(medium2.getId(), cementerio.getId());
        assertDoesNotThrow(() -> {
            serviceM.exorcizar(medium1.getId(), medium2.getId());
        });
    }
    @Test
    void exorcizar_enDistintaUbicacion_lanzaError(){
        conectarEspirituAMedium(medium1, angel);
        conectarEspirituAMedium(medium2, demonio);
        assertThrows(ExorcizarNoPermitidoNoEsMismaUbicacion.class, () -> {
            serviceM.exorcizar(medium1.getId(), medium2.getId());
        });
    }
    @Test
    void exorcizar_AtaqueFallido_AngelPierdeEnergia_MismaUbicacionAlMoverse() {
        Generador.setEstrategia(new GeneradorSecuencial(1, 100));

        angel.setNivelDeConexion(20);
        demonio.setNivelDeConexion(10);

        conectarEspirituAMedium(medium1, angel);
        conectarEspirituAMedium(medium2, demonio);

        serviceM.mover(medium2.getId(), cementerio.getId());

        assertEquals(30, angel.getNivelDeConexion());
        assertEquals(20, demonio.getNivelDeConexion());

        serviceM.exorcizar(medium1.getId(), medium2.getId());

        assertTrue(medium1.getEspiritus().contains(angel),
                "El ángel debería estar conectado al medium exorcista");

        assertTrue(medium2.getEspiritus().contains(demonio),
                "El demonio debería estar conectado al medium exorcizado");

        Optional<Espiritu> angelActualizado = serviceE.recuperar(angel.getId());
        assertEquals(25, angelActualizado.get().getNivelDeConexion(),
                "El ángel debería haber perdido 5 puntos de conexión al fallar el ataque");

        Optional<Espiritu> demonioActualizado = serviceE.recuperar(demonio.getId());
        assertEquals(20, demonioActualizado.get().getNivelDeConexion(),
                "El demonio no debería haber sido afectado en un ataque fallido");
    }

    private void conectarEspirituAMedium(Medium medium, Espiritu espiritu) {
        medium.conectarseAEspiritu(espiritu);
        serviceM.guardar(medium);
        serviceE.guardar(espiritu);
    }

   @AfterEach
   void cleanUp() {
        dataService.eliminarTodo();
   }
}
