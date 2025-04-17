package ar.edu.unq.epersgeist.modelo;

import lombok.Setter;

public class Generador {

    @Setter
    private static GeneradorDeNumeros estrategia = GeneradorRandom.getInstance();

    private Generador() {}

    public static int entre(int min, int max) {
        return estrategia.entre(min, max);
    }

}

