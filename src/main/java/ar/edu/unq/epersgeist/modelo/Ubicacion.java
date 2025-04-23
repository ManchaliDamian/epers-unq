package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
@Entity
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(unique = true, nullable = false)
    private String nombre;

    public Ubicacion(@NonNull String nombre) {
        this.nombre = nombre;
    }

    public void cambiarNombre(String nombre) {
        this.setNombre(nombre);
    }


}
