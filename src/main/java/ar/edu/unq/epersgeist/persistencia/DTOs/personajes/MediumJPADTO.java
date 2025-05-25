package ar.edu.unq.epersgeist.persistencia.DTOs.personajes;

import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "Medium")
public class MediumJPADTO {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private UbicacionJPADTO ubicacion;

    @Column(nullable = false)
    private Integer manaMax;

    @Column(nullable = false)
    @Check(constraints = "mana BETWEEN 0 AND mana_max")
    private Integer mana;

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

    @OneToMany(mappedBy = "mediumConectado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<EspirituJPADTO> espiritus = new HashSet<>();

    public MediumJPADTO(@NotBlank String nombre, @NotNull Integer manaMax, @NotNull Integer mana, @NotNull UbicacionJPADTO ubicacion) {
        if (manaMax < 0) {
            throw new IllegalArgumentException("manaMax no puede ser negativo.");
        }
        if (mana < 0 || mana > manaMax) {
            throw new IllegalArgumentException("mana debe estar entre 0 y manaMax.");
        }
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = mana;
        this.ubicacion = ubicacion;
    }
}