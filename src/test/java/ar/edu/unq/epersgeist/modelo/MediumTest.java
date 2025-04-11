package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MediumTest {
    private EspirituDemoniaco espirituDemoniaco;
    private EspirituAngelical espirituAngelical;
    private Ubicacion quilmes;
    private Ubicacion bernal;
    private Medium mediumConectado;
    private Medium mediumQuilmes;
    private Medium mediumBernal;
    private EspirituDemoniaco espirituNoConectado;
    private EspirituAngelical espirituMock;
    private EspirituDemoniaco demonioMock;
    private GeneradorDeNumeros generadorMock;

    @BeforeEach
    void setUp(){
        quilmes = new Ubicacion("Quilmes");
        bernal = new Ubicacion("Bernal");
        espirituAngelical = new EspirituAngelical(30,"EspirituAngelical",quilmes, generadorMock);
        espirituDemoniaco = new EspirituDemoniaco(25,"EspirituDemoniaco",bernal, generadorMock);
        espirituNoConectado = new EspirituDemoniaco(33,"Belcebú",bernal, generadorMock);
        mediumConectado = new Medium("Mago",100,50,quilmes);
        mediumQuilmes = new Medium("Pepe",100,50,quilmes);
        mediumBernal = new Medium("Bernardo",100,90,bernal);
        espirituDemoniaco.setMediumConectado(mediumConectado);
        espirituAngelical.setMediumConectado(mediumConectado);
        espirituMock = mock(EspirituAngelical.class);
        demonioMock = mock(EspirituDemoniaco.class);
    }

    @Test
    void noSePuedeCrearMediumConManaNegativo(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Medium("Maguin",300, -4, quilmes));
        assertEquals("mana debe estar entre 0 y manaMax.", ex.getMessage());
    }

    @Test
    void noSePuedeCrearMediumConManaMayorAManaMax(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Medium("Maguin",300, 301, quilmes));
        assertEquals("mana debe estar entre 0 y manaMax.", ex.getMessage());
    }

    @Test
    void noSePuedeCrearMediumConManaMaxNegativo(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new Medium("Maguin",-3, 301, quilmes));
        assertEquals("manaMax no puede ser negativo.", ex.getMessage());
    }

    @Test
    void noSePuedeConectarAEspirituQueYaEstaConectadoAOtroMedium(){
        ConectarException ex = assertThrows(ConectarException.class, () -> mediumQuilmes.conectarseAEspiritu(espirituDemoniaco));
        assertEquals("El espiritu [EspirituDemoniaco] no esta conectado al Medium [Pepe]", ex.getMessage());
    }

    @Test
    void noSePuedeConectarAEspirituQueEstaEnDistintaUbicacion(){
        ConectarException ex = assertThrows(ConectarException.class, () -> mediumQuilmes.conectarseAEspiritu(espirituNoConectado));
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
        //setup
        when(espirituMock.estaConectado()).thenReturn(false);
        when(espirituMock.getUbicacion()).thenReturn(quilmes);
        mediumQuilmes.conectarseAEspiritu(espirituMock);
        mediumQuilmes.setMana(90);

        //exercise
        mediumQuilmes.descansar();

        //verify
        assertEquals(100, mediumQuilmes.getMana());
        verify(espirituMock).descansar();
    }

    @Test
    void cuandoSeDescansaSeRecuperaHasta15DeMana(){
        //setup
        when(espirituMock.estaConectado()).thenReturn(false);
        when(espirituMock.getUbicacion()).thenReturn(quilmes);
        mediumQuilmes.conectarseAEspiritu(espirituMock);

        //exercise
        mediumQuilmes.descansar();

        //verify
        assertEquals(65, mediumQuilmes.getMana());
        verify(espirituMock).descansar();
    }

    @Test
    void desvincularseDe(){
        when(espirituMock.estaConectado()).thenReturn(false);
        when(espirituMock.getUbicacion()).thenReturn(bernal);
        mediumBernal.conectarseAEspiritu(espirituNoConectado);
        mediumBernal.conectarseAEspiritu(espirituMock);

        mediumBernal.desvincularseDe(espirituMock);

        verify(espirituMock).desvincularse();
        assertEquals(1, mediumBernal.getEspiritus().size());
        assertEquals(espirituNoConectado, mediumBernal.getEspiritus().stream().findFirst().orElseThrow());
    }

    @Test
    void desvincularseDeNoLanzaErrorCuandoNoHayEspiritusVinculados(){
        assertDoesNotThrow(() -> mediumBernal.desvincularseDe(espirituMock));
    }

    @Test
    void desconectarEspiritu(){
        when(demonioMock.estaConectado()).thenReturn(false);
        when(demonioMock.getUbicacion()).thenReturn(bernal);
        mediumBernal.conectarseAEspiritu(espirituNoConectado);
        mediumBernal.conectarseAEspiritu(demonioMock);

        mediumBernal.desconectarEspiritu(demonioMock);

        assertEquals(1, mediumBernal.getEspiritus().size());
        assertEquals(espirituNoConectado, mediumBernal.getEspiritus().stream().findFirst().orElseThrow());
    }

    @Test
    void exorcizarA(){
        //SETUP
        Medium poseido = new Medium("Juan",100,35, bernal);

        EspirituAngelical angelA = mock(EspirituAngelical.class);
        EspirituAngelical angelB = mock(EspirituAngelical.class);
        EspirituDemoniaco demonioA = mock(EspirituDemoniaco.class);
        EspirituDemoniaco demonioB = mock(EspirituDemoniaco.class);

        when(angelA.estaConectado()).thenReturn(false).thenReturn(true);
        when(angelB.estaConectado()).thenReturn(false).thenReturn(true);
        when(demonioA.estaConectado()).thenReturn(false).thenReturn(true);
        when(demonioB.estaConectado()).thenReturn(false).thenReturn(true);
        when(angelA.getUbicacion()).thenReturn(bernal);
        when(angelB.getUbicacion()).thenReturn(bernal);
        when(demonioA.getUbicacion()).thenReturn(bernal);
        when(demonioB.getUbicacion()).thenReturn(bernal);
        when(angelA.getTipo()).thenReturn(TipoEspiritu.ANGELICAL);
        when(angelB.getTipo()).thenReturn(TipoEspiritu.ANGELICAL);
        when(demonioA.getTipo()).thenReturn(TipoEspiritu.DEMONIACO);
        when(demonioB.getTipo()).thenReturn(TipoEspiritu.DEMONIACO);

        poseido.conectarseAEspiritu(demonioA);
        poseido.conectarseAEspiritu(demonioB);
        mediumBernal.conectarseAEspiritu(angelA);
        mediumBernal.conectarseAEspiritu(angelB);

        // EXERCISE
        mediumBernal.exorcizarA(poseido);

        // VERIFY
        verify(angelA, times(1)).atacar(demonioA);
        verify(angelB, times(1)).atacar(demonioA);
        verify(angelA,never()).atacar(demonioB);
        verify(angelB,never()).atacar(demonioB);
    }
}
