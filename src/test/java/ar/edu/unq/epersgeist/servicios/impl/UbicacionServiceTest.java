package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.dao.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.dao.impl.HibernateUbicacionDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UbicacionServiceTest {

    private UbicacionServiceImpl service;
    private Ubicacion quilmes;
    private Ubicacion bernal;
    private UbicacionDAO ubicacionDao;

    private Espiritu angel;
    private Espiritu demonio;

    @BeforeEach
    void prepare() {
        angel = new Espiritu("angel",10,"damian");
        demonio = new Espiritu("angel",15,"Roberto");
        angel.setUbicacion(quilmes);
        demonio.setUbicacion(quilmes);
        ubicacionDao = new HibernateUbicacionDAO();
        this.service = new UbicacionServiceImpl(ubicacionDao);

        quilmes = new Ubicacion("Quilmes");
        bernal = new Ubicacion("Bernal");
        service.crear(quilmes);
        service.crear(bernal);
    }
//    @Test
//    void espiritusEnUnaUbicacion() {
//        List<Espiritu> espiritusEn = service.espiritusEn(quilmes.getId());
//        assertEquals([], espiritusEn);
//    }
    @Test
    void recuperarUbicacionDada() {
        Ubicacion q = service.recuperar(quilmes.getId());
        assertEquals("Quilmes", q.getNombre());
    }
    @Test
    void recuperarTodasLasUbicaciones() {
        List<Ubicacion> ubicaciones = service.recuperarTodos();
        assertEquals(2, ubicaciones.size());
    }
    @Test
    void actualizarUnaUbicacion(){
        Ubicacion q = service.recuperar(quilmes.getId());
        service.actualizar(q.getId(), "Avellaneda");
        Ubicacion nombreNuevo = service.recuperar(quilmes.getId());

        assertEquals("Avellaneda", nombreNuevo.getNombre());
    }
    @Test
    void eliminarUbicacion() {
        service.eliminar(quilmes);
        List<Ubicacion> ubicaciones = service.recuperarTodos();
        assertEquals(1, ubicaciones.size());
    }

    @AfterEach
    void cleanup() {
        service.eliminarTodo();
    }


}
