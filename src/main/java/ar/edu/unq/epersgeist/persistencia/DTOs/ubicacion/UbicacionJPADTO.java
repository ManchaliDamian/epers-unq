package ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion;

import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter @Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_ubicacion")

@Entity(name = "Ubicacion")
public abstract class UbicacionJPADTO {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(unique = true, nullable = false)
    private String nombre;

    @Column(name = "flujo_de_energia", nullable = false)
    private Integer flujoDeEnergia;

    @Enumerated(EnumType.STRING)
    @Column(name="tipo_ubicacion", insertable=false, updatable=false)
    private TipoUbicacion tipo;

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

    public UbicacionJPADTO(@NonNull String nombre, @NonNull Integer flujoDeEnergia, @NonNull TipoUbicacion tipo) {
        if (flujoDeEnergia < 0 || flujoDeEnergia > 100) {
            throw new IllegalArgumentException("El flujo de energía debe estar entre 0 y 100");
        }
        this.nombre = nombre;
        this.flujoDeEnergia = flujoDeEnergia;
        this.tipo = tipo;
    }

}