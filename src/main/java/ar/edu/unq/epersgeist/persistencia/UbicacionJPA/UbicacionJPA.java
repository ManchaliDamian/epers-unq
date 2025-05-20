package ar.edu.unq.epersgeist.persistencia.UbicacionJPA;

import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Setter @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode @ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_ubicacion")

@Entity
public abstract class UbicacionJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public UbicacionJPA(String nombre, Integer flujoDeEnergia, TipoUbicacion tipo) {
        this.nombre = nombre;
        this.flujoDeEnergia = flujoDeEnergia;
        this.tipo = tipo;
    }

}