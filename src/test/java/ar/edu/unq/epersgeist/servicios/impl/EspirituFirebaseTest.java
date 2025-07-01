package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.*;
import ar.edu.unq.epersgeist.servicios.interfaces.DataService;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class EspirituFirebaseTest {

    @Autowired
    private EspirituService serviceE;
    @Autowired private UbicacionService serviceU;
    @Autowired private MediumService serviceM;

    @Autowired private DataService dataService;


    private Ubicacion quilmes;
    private Ubicacion berazategui;

    private Coordenada c1;
    private Coordenada c4;
    private Coordenada c3;
    private Coordenada c2;
    private Coordenada c5;
    private Coordenada c6;
    private Coordenada c7;
    private Coordenada c8;
    private Coordenada distanciaMas2Km;
    private Poligono poligono;
    private Poligono poligono1;

    @BeforeEach
    void setUp() {
        c1 = new Coordenada(0.0,0.0);
        c2 = new Coordenada(0.0,1.0);
        c3 = new Coordenada(1.0,1.0);
        c4 = new Coordenada(1.0,0.0);
        List<Coordenada> coordenadas = Arrays.asList(c1, c2, c3, c4, c1);
        poligono = new Poligono(coordenadas);
        distanciaMas2Km = new Coordenada(0.0, 0.0314);


        c5 = new Coordenada(2.0,2.0);
        c6 = new Coordenada(2.0,3.0);
        c7 = new Coordenada(3.0,3.0);
        c8 = new Coordenada(3.0,2.0);
        List<Coordenada> coordenadas1 = Arrays.asList(c5, c6, c7, c8, c5);
        poligono1 = new Poligono(coordenadas1);

        quilmes = new Santuario("Quilmes", 100);
        berazategui = new Cementerio("Berazategui",100);

        quilmes = serviceU.guardar(quilmes, poligono);
        berazategui = serviceU.guardar(berazategui, poligono1);




    }



    @Test
    void combateMuchosEspiritus() throws InterruptedException {

        dataService.crearYGuardarEspiritusAngelicales(10, quilmes, c1);

        int VIDA_MINIMA_PARA_COMBATIR = 30; // Vida mínima para ser considerado para el combate
        int NUM_RONDAS_DE_COMBATE = 4; // Número de veces que se repite la ronda de combates
        int COMBATES_POR_RONDA = 13; // Número de combates aleatorios por ronda
        Random random = new Random();

        for (int ronda = 1; ronda <= NUM_RONDAS_DE_COMBATE; ronda++) {
            List<Espiritu> espiritusACombatir = dataService.recuperarTodosMayorVida(VIDA_MINIMA_PARA_COMBATIR);

            if (espiritusACombatir.size() < 2) {
                break;
            }
            // 3. Generar y ejecutar los combates para esta ronda
            IntStream.range(0, COMBATES_POR_RONDA)
                    .filter(i -> espiritusACombatir.size() >= 2)
                    .forEach(i -> {
                        try {
                            // Seleccionar atacante y oponente de la lista de activos
                            Espiritu atacante = espiritusACombatir.get(random.nextInt(espiritusACombatir.size()));
                            Espiritu oponente;
                            do {
                                oponente = espiritusACombatir.get(random.nextInt(espiritusACombatir.size()));
                            } while (oponente.getId().equals(atacante.getId()));
                            // el unico problema que le veo es que dentro de la ronda lo pueden atacar muchas
                            // veces a uno y puede quedar en 0. podemos dejarlo asi o restringirlo para q si
                            // tiene menos vida o si fue atacado...

                            // Ejecutar combate y pausa
                            serviceE.combatir(atacante.getId(), oponente.getId());
                            Thread.sleep(100);
                        } catch (Exception e) {
                            // Manejo de excepciones, sin imprimir
                        }
                    });
            Thread.sleep(2000);
        }

        List<Espiritu> espiritusFinales = dataService.recuperarTodosMayorVida(0); // Recupera todos
        // los que tengan vida > 0
        assertFalse(espiritusFinales.isEmpty(), "Debería haber al menos un espíritu " +
                "con vida al final de la simulación.");
    }

    @AfterEach
    void cleanup() {
        dataService.eliminarTodo();
    }
}
