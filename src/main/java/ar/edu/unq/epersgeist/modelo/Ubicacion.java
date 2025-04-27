package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import java.util.*;

@Getter @Setter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode @ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_ubicacion")
@Entity
public abstract class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Column(nullable = false)
    @Check(constraints = "flujo_de_energia BETWEEN 0 AND 100")
    private Integer flujoDeEnergia;

    public Ubicacion(@NonNull String nombre, @NonNull Integer flujoDeEnergia) {
        if (flujoDeEnergia < 0 || flujoDeEnergia > 100) {
            throw new IllegalArgumentException("El flujo de energía debe estar entre 0 y 100");
        }
        this.nombre = nombre;
        this.flujoDeEnergia = flujoDeEnergia;
    }

    public void cambiarNombre(String nombre) {
        this.setNombre(nombre);
    }

    public Integer getCantidadRecuperada() {
        return (int) (getFlujoDeEnergia() * getMultiplicadorMana());
    }
    protected abstract double getMultiplicadorMana();

    public abstract void invocarAngel (Espiritu espiritu);
    public abstract void invocarDemonio(Espiritu espiritu);
    public  void recuperarConexionComoAngel(Espiritu espiritu){};
    public  void recuperarConexionComoDemonio(Espiritu espiritu){};
    public abstract void moverAngel(Espiritu espiritu);
    public abstract void moverDemonio(Espiritu espiritu);
}
