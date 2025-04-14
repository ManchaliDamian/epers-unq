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

    @OneToMany(mappedBy = "ubicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Espiritu> espiritusUbicados;

    @OneToMany(mappedBy = "ubicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medium> mediumsUbicados;

    public Ubicacion(@NonNull String nombre) {
        this.nombre = nombre;
    }

    public void cambiarNombre(String nombre) {
        this.setNombre(nombre);
    }
    public void agregarEspirituUbicado(Espiritu espiritu) {
        espiritu.setUbicacion(this);
        this.espiritusUbicados.add(espiritu);
    }
    public void agregarMediumUbicado(Medium medium) {
        medium.setUbicacion(this);
        this.mediumsUbicados.add(medium);
    }

}
