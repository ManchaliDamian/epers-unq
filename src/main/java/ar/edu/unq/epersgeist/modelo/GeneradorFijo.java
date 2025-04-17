package ar.edu.unq.epersgeist.modelo;

public class GeneradorFijo implements GeneradorDeNumeros {

    private final int valorFijo;

    public GeneradorFijo(int valorFijo) {
        this.valorFijo = valorFijo;
    }

    @Override
    public int entre(int min, int max) {
        return valorFijo;
    }
}


