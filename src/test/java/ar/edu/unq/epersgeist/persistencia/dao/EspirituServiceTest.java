package ar.edu.unq.epersgeist.persistencia.dao;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.TipoEspiritu;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateEspirituDAO;
import ar.edu.unq.epersgeist.servicios.EspirituService;
import ar.edu.unq.epersgeist.servicios.impl.EspirituServiceImpl;
import ar.edu.unq.epersgeist.servicios.runner.HibernateTransactionRunner;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EspirituServiceTest {
    @Test
    void testEspiritusDemoniacos() {
        EspirituDAO espirituDAO = new HibernateEspirituDAO();
        EspirituService service = new EspirituServiceImpl(espirituDAO);

        // Creo espíritus de prueba
        Espiritu demonio1 = new Espiritu("Azazel", TipoEspiritu.DEMONIACO, 80);
        Espiritu demonio2 = new Espiritu("Belcebu", TipoEspiritu.DEMONIACO, 100);
        Espiritu angel = new Espiritu("Gabriel", TipoEspiritu.ANGELICAL, 90);

        HibernateTransactionRunner.runTrx(() -> {
            espirituDAO.guardar(demonio1);
            espirituDAO.guardar(demonio2);
            espirituDAO.guardar(angel);
            return null;
        });

        // Ejecuto método bajo test
        List<Espiritu> demonios = service.espiritusDemoniacos();

        // Verificaciones
        assertEquals(2, demonios.size());
        assertTrue(demonios.stream().allMatch(e -> e.getTipo() == TipoEspiritu.DEMONIACO));
        assertEquals("Belcebu", demonios.get(0).getNombre()); // Mayor conexión primero
        assertEquals("Azazel", demonios.get(1).getNombre());
    }

}
