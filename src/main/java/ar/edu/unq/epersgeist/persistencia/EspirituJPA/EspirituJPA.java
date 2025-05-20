package ar.edu.unq.epersgeist.persistencia.EspirituJPA;


import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import ar.edu.unq.epersgeist.persistencia.UbicacionJPA.UbicacionJPA;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.*;

import java.util.Date;

@Getter @Setter @NoArgsConstructor(force = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

@Entity
public abstract class EspirituJPA {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private UbicacionJPA ubicacion;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "nivel_de_conexion BETWEEN 0 AND 100")
    protected Integer nivelDeConexion;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "medium_id")
    private Medium mediumConectado;

    private final TipoEspiritu tipo;

    //auditoria
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @Column(nullable = false)
    private boolean deleted = false;

    public EspirituJPA (@NonNull String nombre, @NonNull UbicacionJPA ubicacion, @NonNull TipoEspiritu tipo) {

        this.nivelDeConexion = 0;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.tipo = tipo;


    }


}
