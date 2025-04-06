package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;
import ar.edu.unq.epersgeist.modelo.exception.NivelDeConexionException;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.*;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode @ToString

@Entity
public final class Espiritu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEspiritu tipo;

    @Column(nullable = false) @ColumnDefault("0")
    @Check(constraints = "nivelDeConexion BETWEEN 0 AND 100")

    private Integer nivelDeConexion;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Ubicacion ubicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medium mediumConectado;

    public Espiritu(@NonNull TipoEspiritu tipo, @NonNull Integer nivelDeConexion, @NonNull String nombre, @NonNull Ubicacion ubicacion) {
        validarNivelDeConexion(nivelDeConexion);
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    // CONSULTAR POR ESTA SOLUCION
    /*public Espiritu(@NonNull Long id, @NonNull TipoEspiritu tipo, @NonNull Integer nivelDeConexion, @NonNull String nombre) {
        validarNivelDeConexion(nivelDeConexion);
        this.id = id;
        this.tipo = tipo;
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
    }*/

    public void aumentarConexion(Medium medium) {
        if (this.mediumConectado != medium){
            throw new ConectarException(this);
        }
        int aumento = (int) Math.round(medium.getMana() * 0.20);

        nivelDeConexion = Math.min(nivelDeConexion + aumento, 100);
    }

    private static void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < 0 || nivelDeConexion > 100) {
            throw new NivelDeConexionException();
        }
    }

    public boolean estaLibre() {
        return this.mediumConectado == null;
    }
}
