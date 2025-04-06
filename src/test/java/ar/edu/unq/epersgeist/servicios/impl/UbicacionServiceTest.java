package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UbicacionServiceTest {

    private UbicacionServiceImpl service;
    private Ubicacion ubi;

    @BeforeEach
    void prepare() {
        this.service = new UbicacionServiceImpl();

        ubi = new Ubicacion("Quilmes");
        service.crear(ubi);

    }



}
