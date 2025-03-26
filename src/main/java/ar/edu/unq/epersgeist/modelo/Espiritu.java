package ar.edu.unq.epersgeist.modelo;

import java.io.Serializable;

public class Espiritu implements Serializable {

    private Long id;
    private String tipo;
    private Integer nivelDeConexion;
    private String nombre;

    public Espiritu(String tipo, Integer nivelDeConexion, String nombre) {
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }

    // CONSULTAR POR ESTA SOLUCION
    public Espiritu(Long id, String tipo, Integer nivelDeConexion, String nombre) {
        this.id = id;
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }

    public void aumentarConexion(Medium medium) {
        nivelDeConexion = nivelDeConexion + 10;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Integer getNivelDeConexion() {
        return nivelDeConexion;
    }

    public String getNombre() {
        return nombre;
    }
}