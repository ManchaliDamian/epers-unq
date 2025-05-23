package ar.edu.unq.epersgeist.persistencia.DTOs.personajes;

import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;

import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.*;

import java.util.Date;

@Getter @Setter @ToString
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EqualsAndHashCode

@Entity(name = "Espiritu")
public abstract class EspirituJPADTO {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private UbicacionJPADTO ubicacion;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "nivel_de_conexion BETWEEN 0 AND 100")
    private Integer nivelDeConexion;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "medium_id")
    private MediumJPADTO mediumConectado;

    @Enumerated(EnumType.STRING)
    private TipoEspiritu tipo;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(nullable = false)
    private boolean deleted = false;

    public EspirituJPADTO (@NotBlank String nombre, @NonNull UbicacionJPADTO ubicacion, @NonNull TipoEspiritu tipo) {
        this.nivelDeConexion = 0;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.tipo = tipo;
    }
}
