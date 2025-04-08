package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
@Getter @Setter @NoArgsConstructor @ToString

@Entity
public class Medium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;


    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(nullable = false)
    private Integer manaMax;

    @Column(nullable = false)
    @Check(constraints = "mana BETWEEN 0 AND manaMax")
    private Integer mana;


    @OneToMany(mappedBy = "mediumConectado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final Set<Espiritu> espiritus = new HashSet<>();

    public Medium(String nombre, Integer manaMax, Integer mana, Ubicacion ubicacion) {
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

    public void conectarseAEspiritu(Espiritu espiritu) {
        if ((!this.ubicacion.equals(espiritu.getUbicacion())) || !espiritu.estaLibre()){
            throw new ConectarException(espiritu, this);
        }
        espiritus.add(espiritu);
        espiritu.setMediumConectado(this);
    }

    public void descansar() {
        this.setMana(
                Math.min(this.getMana() + 15, this.getManaMax())
        );
    }
}