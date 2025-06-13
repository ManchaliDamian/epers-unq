package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.exception.BadRequest.ConectarException;
import ar.edu.unq.epersgeist.exception.Conflict.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.exception.Conflict.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MediumTest {
    private EspirituDemoniaco espirituDemoniaco;
    private EspirituAngelical espirituAngelical;
    private Ubicacion santuario;
    private Ubicacion cementerio;
    private Medium mediumConectado;
    private Medium mediumQuilmes;
    private Medium mediumBernal;
    private EspirituDemoniaco espirituNoConectado;
    private EspirituAngelical espirituAngelicalMock;


    @BeforeEach
    void setUp(){
        santuario = new Santuario("Quilmes", 70);
        cementerio = new Cementerio("Bernal",60);
        espirituAngelical = new EspirituAngelical("EspirituAngelical",santuario);
        espirituDemoniaco = new EspirituDemoniaco("EspirituDemoniaco",cementerio);
        espirituNoConectado = new EspirituDemoniaco("Belcebú", cementerio);
        mediumConectado = new Medium("Mago",100,50,santuario);
        mediumQuilmes = new Medium("Pepe",100,50,santuario);
        mediumBernal = new Medium("Bernardo",100,90,cementerio);
        espirituDemoniaco.setMediumConectado(mediumConectado);
        espirituAngelical.setMediumConectado(mediumConectado);
        espirituAngelicalMock = mock(EspirituAngelical.class);

    }

    @Test
    void noSePuedeCrearMediumConManaNegativo(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Medium("Maguin",300, -4, santuario));
        assertEquals("mana debe estar entre 0 y manaMax.", ex.getMessage());
    }

    @Test
    void noSePuedeCrearMediumConManaMayorAManaMax(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Medium("Maguin",300, 301, santuario));
        assertEquals("mana debe estar entre 0 y manaMax.", ex.getMessage());
    }

    @Test
    void noSePuedeCrearMediumConManaMaxNegativo(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Medium("Maguin",-3, 301, santuario));
        assertEquals("manaMax no puede ser negativo.", ex.getMessage());
    }

    @Test
    void noSePuedeConectarAEspirituQueYaEstaConectadoAOtroMedium(){
        assertThrows(ConectarException.class, () -> mediumBernal.conectarseAEspiritu(espirituDemoniaco));
    }

    @Test
    void noSePuedeConectarAEspirituQueEstaEnDistintaUbicacion(){
        assertThrows(EspirituNoEstaEnLaMismaUbicacionException.class, () -> mediumQuilmes.conectarseAEspiritu(espirituNoConectado));
    }

    @Test
    void cuandoSeConectaAEspirituSeConocenMutuamente(){
        mediumBernal.conectarseAEspiritu(espirituNoConectado);
        assertEquals(1, mediumBernal.getEspiritus().size());
        assertEquals(espirituNoConectado, mediumBernal.getEspiritus().stream().findFirst().orElseThrow());
        assertEquals(mediumBernal, espirituNoConectado.getMediumConectado());
    }

    @Test
    void cuandoSeDescansaNoSeSobrepasaManaMax(){
        mediumBernal.conectarseAEspiritu(espirituNoConectado);
        mediumBernal.descansar();
        assertEquals(100, mediumBernal.getMana());
    }

    @Test
    void desvincularseDe(){
        when(espirituAngelicalMock.estaConectado()).thenReturn(false);
        when(espirituAngelicalMock.getUbicacion()).thenReturn(cementerio);
        mediumBernal.conectarseAEspiritu(espirituNoConectado);
        mediumBernal.conectarseAEspiritu(espirituAngelicalMock);

        mediumBernal.desvincularseDe(espirituAngelicalMock);

        assertEquals(1, mediumBernal.getEspiritus().size());
        assertEquals(espirituNoConectado, mediumBernal.getEspiritus().stream().findFirst().orElseThrow());
    }

    @Test
    void desvincularseDeNoLanzaErrorCuandoNoHayEspiritusVinculados(){
        assertDoesNotThrow(() -> mediumBernal.desvincularseDe(espirituAngelicalMock));
    }

    @Test
    void exorcizarA_SinAngeles() {
        Medium poseido = new Medium("Juan",100,35, cementerio);

        List<EspirituAngelical> angeles = new ArrayList<EspirituAngelical>();
        List<EspirituDemoniaco> demoniacos = new ArrayList<EspirituDemoniaco>();

        assertThrows(ExorcistaSinAngelesException.class, () -> poseido.exorcizarA(angeles, demoniacos, cementerio));

    }
    //ejemplos del enunciado
    @Test
    void descansarEnCementerioAumentaManaCorrectamente() {
        Ubicacion cementerio = new Cementerio("cementerio",100);
        Medium yohAsakura = new Medium("Yoh Asakura", 200, 10, cementerio);
        yohAsakura.descansar();
        assertEquals(60, yohAsakura.getMana());
    }

    @Test
    void descansarEnSantuarioAumentaManaCorrectamente() {
        Ubicacion santuario = new Santuario("santuario",100);
        Medium lorraineWaine = new Medium("Lorraine Waine", 200, 10, santuario);
        lorraineWaine.descansar();
        assertEquals(160, lorraineWaine.getMana());
    }

}
