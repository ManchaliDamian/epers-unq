package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.TipoEspiritu;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.*;

public class EspirituServiceTest {

    private EspirituService service;
    private EspirituDAO espirituDAO;

    private Espiritu demonio1;
    private Espiritu demonio2;
    private Espiritu angel;

    @BeforeEach
    void setUp() {
        espirituDAO = new HibernateEspirituDAO();
        service = new EspirituServiceImpl(espirituDAO);

        demonio1 = new Espiritu(TipoEspiritu.DEMONIACO, 80, "Azazel");
        demonio2 = new Espiritu(TipoEspiritu.DEMONIACO, 100, "Belcebu");
        angel = new Espiritu(TipoEspiritu.ANGELICAL, 90, "Gabriel");

        service.guardar(demonio1);
         service.guardar(demonio2);
         service.guardar(angel);
    }

    @Test
    void testEspiritusDemoniacos() {
        List<Espiritu> demonios = service.espiritusDemoniacos();

        assertEquals(2, demonios.size());
        assertTrue(demonios.stream().allMatch(e -> e.getTipo() == TipoEspiritu.DEMONIACO));
        assertEquals("Belcebu", demonios.get(0).getNombre());
        assertEquals("Azazel", demonios.get(1).getNombre());
    }


}
