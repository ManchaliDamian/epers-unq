package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.NivelDeConexionException;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.*;

import java.io.Serializable;
import static java.lang.Math.max;


@Getter @Setter @NoArgsConstructor @ToString

@Entity
public class Espiritu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false) @ColumnDefault("0")
    @Check(constraints = "nivel_de_conexion BETWEEN 0 AND 100")
    //No me funciono ninguna de las dos. Estas serian a nivel Java y la de arriba a nivel BD
    // @Range(min = 0, max = 100)
    // @Min(0) @Max(100)
    private Integer nivelDeConexion;

    @Column(nullable = false)
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
