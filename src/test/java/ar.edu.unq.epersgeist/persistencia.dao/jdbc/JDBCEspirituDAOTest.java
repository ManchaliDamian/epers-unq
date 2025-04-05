package ar.edu.unq.epersgeist.persistencia.dao.jdbc;

class JDBCEspirituDAOTest {
/*
    private JDBCEspirituDAO espirituDAO;
    private Espiritu espiritu_1;
    private Espiritu espiritu_2;
    private Medium medium;

    @BeforeEach
    void setUp() {
        espirituDAO = new JDBCEspirituDAO();
        espiritu_1 = espirituDAO.crear(new Espiritu("Fantasma", 5, "Pablo Fidel"));
        espiritu_2 = espirituDAO.crear(new Espiritu("Oni", 2, "Warmi"));
        medium = new Medium("Roberto", 100, 20);
    }
    @AfterEach
    void tearDown() throws SQLException {
        espirituDAO.eliminar(espiritu_1.getId());
        espirituDAO.eliminar(espiritu_2.getId());
    }

    @Test
    void crearEspiritu() {
        Espiritu creado = espirituDAO.crear(new Espiritu("Angel", 5, "Juan"));

        assertNotNull(creado.getId());
        assertEquals("Angel", creado.getTipo());
        assertEquals(5, creado.getNivelDeConexion());
        espirituDAO.eliminar(creado.getId());
    }
    @Test
    void recuperarEspiritu() {
        Espiritu creado = (espiritu_1);

        Espiritu recuperado = espirituDAO.recuperar(creado.getId());

        assertNotNull(recuperado);
        assertEquals(creado.getId(), recuperado.getId());
        assertEquals("Pablo Fidel", recuperado.getNombre());
    }
    @Test
    void errorRecuperarEspiritu() {

        Espiritu recuperado = espirituDAO.recuperar(1L);
        assertNull(recuperado);
    }

    @Test
    void recuperarTodos() {
        List<Espiritu> espiritus = espirituDAO.recuperarTodos();
        assertEquals(2, espiritus.size());
    }

    @Test
    void actualizarEspiritu() {
        Espiritu espirituOriginal = espirituDAO.crear(espiritu_1);

        espirituOriginal.aumentarConexion(medium);
        espirituDAO.actualizar(espirituOriginal);

        assertEquals(15, espirituOriginal.getNivelDeConexion());

        Espiritu espirituPersistido = espirituDAO.recuperar(espirituOriginal.getId());
        assertNotNull(espirituPersistido);
        assertEquals(15, espirituPersistido.getNivelDeConexion());
        assertEquals("Pablo Fidel", espirituPersistido.getNombre());
        assertEquals("Fantasma", espirituPersistido.getTipo());

        assertNotSame(espirituOriginal, espirituPersistido);
    }

    @Test
    void eliminarEspiritu() {
        Espiritu creado = espirituDAO.crear(espiritu_1);
        espirituDAO.eliminar(creado.getId());
        assertNull(espirituDAO.recuperar(creado.getId()));
    }

    */
}
