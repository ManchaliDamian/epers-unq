package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;
import ar.edu.unq.epersgeist.modelo.exception.NivelDeConexionException;
import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituOcupado;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.*;
import static java.lang.Math.max;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode @ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

@Entity
public abstract class Espiritu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "nivelDeConexion BETWEEN 0 AND 100")

    private Integer nivelDeConexion;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private TipoEspiritu tipo;

    @ManyToOne
    @JoinColumn(name = "medium_id")
    private Medium mediumConectado;

    public Espiritu (@NonNull Integer nivelDeConexion, @NonNull String nombre, @NonNull Ubicacion ubicacion) {
        validarNivelDeConexion(nivelDeConexion);
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    // CONSULTAR POR ESTA SOLUCION
    /*public Espiritu(@NonNull Long id, @NonNull Integer nivelDeConexion, @NonNull String nombre) {
        validarNivelDeConexion(nivelDeConexion);
        this.id = id;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }*/

    public void aumentarConexion(Medium medium) {
        if (this.mediumConectado != medium){
            throw new ConectarException(this, medium);
        }
        int aumento = (int) Math.round(medium.getMana() * 0.20);

        nivelDeConexion = Math.min(nivelDeConexion + aumento, 100);
    }

    private static void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < 0 || nivelDeConexion > 100) {
            throw new NivelDeConexionException();
        }
    }

    public void validarConexion(Medium medium){
        this.validarDisponibilidad();
    }

    public void validarDisponibilidad(){
        if(!this.estaLibre()){
            throw new ExceptionEspirituOcupado(this);
        }
    }
    public void perderNivelDeConexion(int cantidad){
        this.nivelDeConexion = max(this.getNivelDeConexion() - cantidad, 0);
    }

    //Dudas
    //public abstract boolean puedeExorcizar();
    public boolean estaLibre() {
        return this.mediumConectado == null;
    }
}
