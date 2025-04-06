package ar.edu.unq.epersgeist.modelo;

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
    //No me funciono ninguna de las dos. Estas serian a nivel Java y la de arriba a nivel BD
    // @Range(min = 0, max = 100)
    // @Min(0) @Max(100)
    private Integer nivelDeConexion;

    @Column(nullable = false)
    private String nombre;


    public Espiritu(@NonNull TipoEspiritu tipo, @NonNull Integer nivelDeConexion, @NonNull String nombre) {
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

    public void aumentarConexion(Medium medium) {
        nivelDeConexion = Math.min(nivelDeConexion + 10, 100);
    }

    private static void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < 0 || nivelDeConexion > 100) {
            throw new IllegalArgumentException("El nivel de conexión debe ser entre 0 y 100.");
        }
    }
}
