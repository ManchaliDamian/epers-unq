package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
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

    private Coordenada c1;
    private Coordenada c4;
    private Coordenada c3;
    private Coordenada c2;
    private Poligono poligono;



    @BeforeEach
    void setUp(){
        c1 = new Coordenada(1.0,1.0);
        c2 = new Coordenada(2.0,2.0);
        c3 = new Coordenada(3.0,3.0);
        c4 = new Coordenada(-1.0,-1.0);
        List<Coordenada> coordenadas = Arrays.asList(c1, c2, c3, c4, c1);
        poligono = new Poligono(coordenadas);
        santuario = new Santuario("Quilmes", 70, poligono);
        cementerio = new Cementerio("Bernal",60, poligono);
        espirituAngelical = new EspirituAngelical("EspirituAngelical",santuario, c1);
        espirituDemoniaco = new EspirituDemoniaco("EspirituDemoniaco",cementerio,c2);
        espirituNoConectado = new EspirituDemoniaco("Belcebú",cementerio,c3);
        mediumConectado = new Medium("Mago",100,50,santuario,c1);
        mediumQuilmes = new Medium("Pepe",100,50,santuario, c2);
        mediumBernal = new Medium("Bernardo",100,90,cementerio,c1);
        espirituDemoniaco.setMediumConectado(mediumConectado);
        espirituAngelical.setMediumConectado(mediumConectado);
        espirituAngelicalMock = mock(EspirituAngelical.class);

    }

    @Test
    void noSePuedeCrearMediumConManaNegativo(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Medium("Maguin",300, -4, santuario, c1));
        assertEquals("mana debe estar entre 0 y manaMax.", ex.getMessage());
    }

    @Test
    void noSePuedeCrearMediumConManaMayorAManaMax(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Medium("Maguin",300, 301, santuario, c1));
        assertEquals("mana debe estar entre 0 y manaMax.", ex.getMessage());
    }

    @Test
    void noSePuedeCrearMediumConManaMaxNegativo(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Medium("Maguin",-3, 301, santuario, c1));
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
        Medium poseido = new Medium("Juan",100,35, cementerio,c1);

        List<EspirituAngelical> angeles = new ArrayList<EspirituAngelical>();
        List<EspirituDemoniaco> demoniacos = new ArrayList<EspirituDemoniaco>();

        assertThrows(ExorcistaSinAngelesException.class, () -> poseido.exorcizarA(angeles, demoniacos, cementerio));

    }
    //ejemplos del enunciado
    @Test
    void descansarEnCementerioAumentaManaCorrectamente() {
        Ubicacion cementerio = new Cementerio("cementerio",100, poligono);
        Medium yohAsakura = new Medium("Yoh Asakura", 200, 10, cementerio, c1);
        yohAsakura.descansar();
        assertEquals(60, yohAsakura.getMana());
    }

    @Test
    void descansarEnSantuarioAumentaManaCorrectamente() {
        Ubicacion santuario = new Santuario("santuario",100, poligono);
        Medium lorraineWaine = new Medium("Lorraine Waine", 200, 10, santuario,c1);
        lorraineWaine.descansar();
        assertEquals(160, lorraineWaine.getMana());
    }

}
