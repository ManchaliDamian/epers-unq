package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(unique = true, nullable = false)
    private String nombre;
    private List<Espiritu> espiritusUbicados;
    private List<Medium> mediumsUbicados;

    public Ubicacion(@NonNull String nombre) {
        this.nombre = nombre;
    }

}
