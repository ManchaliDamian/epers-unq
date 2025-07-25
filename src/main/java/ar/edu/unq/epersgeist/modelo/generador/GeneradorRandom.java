package ar.edu.unq.epersgeist.modelo.generador;

import java.util.Random;

public class GeneradorRandom implements GeneradorDeNumeros {

    private final Random random;

    public GeneradorRandom() {
        this.random = new Random();
    }

    @Override
    public int entre(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
