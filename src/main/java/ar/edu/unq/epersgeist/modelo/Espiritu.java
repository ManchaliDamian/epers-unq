package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.NivelDeConexionException;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import static java.lang.Math.max;

    @Getter @Setter

public abstract class Espiritu implements Serializable {

    private Integer nivelDeConexion;

    @id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    private Medium mediumConectado;


    private static void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < 0 || nivelDeConexion > 100) {
            throw new IllegalArgumentException("El nivel de conexión debe ser entre 0 y 100.");
        }
    }

    public Espiritu(String tipo, Integer nivelDeConexion, String nombre) {
        validarNivelDeConexion(nivelDeConexion);
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }

    // CONSULTAR POR ESTA SOLUCION
    public Espiritu(Long id, String tipo, Integer nivelDeConexion, String nombre) {
        validarNivelDeConexion(nivelDeConexion);
        this.id = id;
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }

    public void validarConexion(Medium medium){
        this.validarDisponibilidad();
    }

    public void validarDisponibilidad(){
        if(!this.estaLibre()){
            throw new ExceptionEspirituOcupado(this);
        }
    }

    public boolean estaLibre() {
        return this.mediumConectado == null;
    }

    public void perderNivelDeConexion(int cantidad){
       this.nivelDeConexion = max(this.getNivelDeConexion() - cantidad, 0);
    }

    //Dudas
    public abstract boolean puedeExorcizar();

    public void aumentarConexion(Medium medium) {
        nivelDeConexion = Math.min(nivelDeConexion + 10, 100);
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