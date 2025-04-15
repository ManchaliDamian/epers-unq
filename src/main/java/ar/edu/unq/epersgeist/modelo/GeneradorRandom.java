package ar.edu.unq.epersgeist.modelo;

import java.util.Random;

public class GeneradorRandom implements GeneradorDeNumeros {

    private static GeneradorRandom instance;
    private final Random random;

    private GeneradorRandom() {
        this.random = new Random();
    }

    public static GeneradorRandom getInstance() {
        if (instance == null) {
            instance = new GeneradorRandom();
        }
        return instance;
    }

    @Override
    public int entre(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}

