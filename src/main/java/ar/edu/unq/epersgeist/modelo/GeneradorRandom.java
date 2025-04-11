package ar.edu.unq.epersgeist.modelo;

import java.util.Random;

public class GeneradorRandom implements GeneradorDeNumeros {

    private Random random = new Random();

    @Override
    public int entre(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}

