package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.NivelDeConexionException;
import lombok.*;
import jakarta.persistence.*;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @ToString

@Entity
public class Espiritu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private Integer nivelDeConexion;
    private String nombre;


    public Espiritu(@NonNull String tipo, @NonNull Integer nivelDeConexion, @NonNull String nombre) {
        validarNivelDeConexion(nivelDeConexion);
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }

    // CONSULTAR POR ESTA SOLUCION
    public Espiritu(@NonNull Long id, @NonNull String tipo, @NonNull Integer nivelDeConexion, @NonNull String nombre) {
        validarNivelDeConexion(nivelDeConexion);
        this.id = id;
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }

    public void aumentarConexion(Medium medium) {
        nivelDeConexion = Math.min(nivelDeConexion + 10, 100);
    }

    private static void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < 0 || nivelDeConexion > 100) {
            throw new IllegalArgumentException("El nivel de conexión debe ser entre 0 y 100.");
        }
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
