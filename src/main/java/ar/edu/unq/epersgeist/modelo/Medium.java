package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Medium implements Serializable {

    private String nombre;
    private Integer manaMax;
    private Integer mana;
    private Set<Espiritu> espiritus = new HashSet<>();
    private Ubicacion ubicacion;

    public Medium(String nombre, Integer manaMax, Integer mana, Ubicacion ubicacion) {
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = mana;
        this.ubicacion = ubicacion;
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        if ((!this.ubicacion.equals(espiritu.getUbicacion())) || !espiritu.estaLibre()){
            throw new ConectarException(espiritu);
        }
        espiritus.add(espiritu);
        espiritu.setMediumConectado(this);
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getManaMax() {
        return manaMax;
    }

    public Integer getMana() {
        return mana;
    }

    public Set<Espiritu> getEspiritus() {
        return espiritus;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }
}