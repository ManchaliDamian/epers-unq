package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.NivelDeConexionException;
import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituOcupado;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.*;
import static java.lang.Math.max;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode @ToString

@Entity
public class Espiritu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "nivelDeConexion BETWEEN 0 AND 100")

    private Integer nivelDeConexion;

    @Column(nullable = false)
    private String nombre;

    private Medium mediumConectado;

    public Espiritu(@NonNull String tipo, @NonNull Integer nivelDeConexion, @NonNull String nombre) {
        validarNivelDeConexion(nivelDeConexion);
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }

    // CONSULTAR POR ESTA SOLUCION
    /*public Espiritu(@NonNull Long id, @NonNull TipoEspiritu tipo, @NonNull Integer nivelDeConexion, @NonNull String nombre) {
        validarNivelDeConexion(nivelDeConexion);
        this.id = id;
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }*/

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
    //public abstract boolean puedeExorcizar();

    public void aumentarConexion(Medium medium) {
        nivelDeConexion = Math.min(nivelDeConexion + 10, 100);
    }

    private static void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < 0 || nivelDeConexion > 100) {
            throw new NivelDeConexionException();
        }
    }

}
