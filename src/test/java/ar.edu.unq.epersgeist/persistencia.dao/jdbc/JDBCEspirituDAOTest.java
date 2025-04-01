package ar.edu.unq.epersgeist.persistencia.dao.jdbc;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JDBCEspirituDAOTest {

    private JDBCEspirituDAO espirituDAO;

    @BeforeEach
    void setUp() {
        espirituDAO = new JDBCEspirituDAO();
    }

    @Test
    void crearEspiritu() {
        Espiritu espiritu = new Espiritu("Fantasma", 5, "Pablo Fidel");
        Espiritu creado = espirituDAO.crear(espiritu);

        assertNotNull(creado.getId());
        assertEquals("Fantasma", creado.getTipo());
        assertEquals(5, creado.getNivelDeConexion());
        assertEquals(1L, creado.getId());
    }
    @Test
    void recuperarEspiritu() {
        Espiritu espiritu = new Espiritu("Espectro", 5, "Warmi");
        Espiritu creado = espirituDAO.crear(espiritu);

        Espiritu recuperado = espirituDAO.recuperar(creado.getId());

        assertNotNull(recuperado);
        assertEquals(creado.getId(), recuperado.getId());
        assertEquals("Warmi", recuperado.getNombre());
    }
    @Test
    void errorRecuperarEspiritu() {

        Espiritu recuperado = espirituDAO.recuperar(1L);

        assertNull(recuperado);
    }

    @Test
    void recuperarTodos() {
        espirituDAO.crear(new Espiritu("Oni", 5, "Selene"));
        espirituDAO.crear(new Espiritu("Poltergeist", 8, "Belen"));

        List<Espiritu> espiritus = espirituDAO.recuperarTodos();

        assertEquals(2, espiritus.size());
    }

    @Test
    void actualizarEspiritu() {
        Espiritu espirituOriginal = espirituDAO.crear(new Espiritu("Jantu", 5, "Fidel"));
        Medium medium = new Medium("Roberto", 100, 20);

        espirituOriginal.aumentarConexion(medium);  // Lógica de negocio
        espirituDAO.actualizar(espirituOriginal);    // Persistencia

        assertEquals(15, espirituOriginal.getNivelDeConexion());

        Espiritu espirituPersistido = espirituDAO.recuperar(espirituOriginal.getId());
        assertNotNull(espirituPersistido);
        assertEquals(15, espirituPersistido.getNivelDeConexion());
        assertEquals("Fidel", espirituPersistido.getNombre());
        assertEquals("Jantu", espirituPersistido.getTipo());

        assertNotSame(espirituOriginal, espirituPersistido);
    }

    @Test
    void eliminarEspiritu() {
        Espiritu espiritu = espirituDAO.crear(new Espiritu("Fantasma", 5, "Espiritu a Eliminar"));

        espirituDAO.eliminar(espiritu.getId());

        assertNull(espirituDAO.recuperar(espiritu.getId()));
    }
    @Test
    void errorEliminarEspiritu() {
        Espiritu espiritu = espirituDAO.crear(new Espiritu("Fantasma", 5, "Espiritu a Eliminar"));

        espirituDAO.eliminar(2L); //siguiente al id inicial

        assertNotNull(espirituDAO.recuperar(espiritu.getId()));
    }
}