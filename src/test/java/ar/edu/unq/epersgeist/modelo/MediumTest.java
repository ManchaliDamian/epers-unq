package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.modelo.exception.ExorcistaSinAngelesException;
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
    private EspirituAngelical espirituMock;


    @BeforeEach
    void setUp(){
        santuario = new Santuario("Quilmes", 70);
        cementerio = new Cementerio("Bernal",60);
        espirituAngelical = new EspirituAngelical("EspirituAngelical",santuario);
        espirituDemoniaco = new EspirituDemoniaco("EspirituDemoniaco",cementerio);
        espirituNoConectado = new EspirituDemoniaco("Belcebú",cementerio);
        mediumConectado = new Medium("Mago",100,50,santuario);
        mediumQuilmes = new Medium("Pepe",100,50,santuario);
        mediumBernal = new Medium("Bernardo",100,90,cementerio);
        espirituDemoniaco.setMediumConectado(mediumConectado);
        espirituAngelical.setMediumConectado(mediumConectado);
        espirituMock = mock(EspirituAngelical.class);

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
        //setup
        when(espirituMock.estaConectado()).thenReturn(false);
        when(espirituMock.getUbicacion()).thenReturn(santuario);
        mediumQuilmes.conectarseAEspiritu(espirituMock);
        mediumQuilmes.setMana(90);

        //exercise
        mediumQuilmes.descansar();

        //verify
        assertEquals(100, mediumQuilmes.getMana());
        verify(espirituMock).descansar(santuario);
    }

    @Test
    void cuandoSeDescansaSeRecuperaHasta15DeMana(){
        //setup
        when(espirituMock.estaConectado()).thenReturn(false);
        when(espirituMock.getUbicacion()).thenReturn(santuario);
        mediumQuilmes.conectarseAEspiritu(espirituMock);

        //exercise
        mediumQuilmes.descansar();

        //verify
        assertEquals(65, mediumQuilmes.getMana());
        verify(espirituMock).descansar(santuario);
    }

    @Test
    void desvincularseDe(){
        when(espirituMock.estaConectado()).thenReturn(false);
        when(espirituMock.getUbicacion()).thenReturn(cementerio);
        mediumBernal.conectarseAEspiritu(espirituNoConectado);
        mediumBernal.conectarseAEspiritu(espirituMock);

        mediumBernal.desvincularseDe(espirituMock);

        assertEquals(1, mediumBernal.getEspiritus().size());
        assertEquals(espirituNoConectado, mediumBernal.getEspiritus().stream().findFirst().orElseThrow());
    }

    @Test
    void desvincularseDeNoLanzaErrorCuandoNoHayEspiritusVinculados(){
        assertDoesNotThrow(() -> mediumBernal.desvincularseDe(espirituMock));
    }

    @Test
    void exorcizarA_SinAngeles() {
        Medium poseido = new Medium("Juan",100,35, cementerio);

        List<EspirituAngelical> angeles = new ArrayList<EspirituAngelical>();
        List<EspirituDemoniaco> demoniacos = new ArrayList<EspirituDemoniaco>();

        assertThrows(ExorcistaSinAngelesException.class, () -> poseido.exorcizarA(angeles, demoniacos));

    }
}
