package ar.edu.unq.epersgeist.modelo.generador;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class GeneradorSecuencial implements GeneradorDeNumeros {

    private final Queue<Integer> valores;

    public GeneradorSecuencial(Integer... valores) {
        this.valores = new LinkedList<>(Arrays.asList(valores));
    }

    @Override
    public int entre(int min, int max) {
        return valores.isEmpty() ? min : valores.poll();
    }
}





