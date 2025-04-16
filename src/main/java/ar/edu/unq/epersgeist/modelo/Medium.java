package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituOcupado;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;

import java.util.*;

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
        if ((!this.ubicacion.equals(espiritu.getUbicacion())) || espiritu.estaConectado()){
            throw new ConectarException(espiritu, this);
        }
        espiritus.add(espiritu);
        espiritu.setMediumConectado(this);
    }

    public void descansar() {
        this.setMana(
                Math.min(this.getMana() + 15, this.getManaMax())
        );

        this.getEspiritus().forEach(Espiritu::descansar);
    }

    public void desvincularseDe(Espiritu espiritu){
        espiritu.desvincularse();
        this.getEspiritus().remove(espiritu);
    }

    public void exorcizarA(List<EspirituAngelical> angeles, List<EspirituDemoniaco> demonios){
        int i = 0;
        int cantAngeles = angeles.size();
        while (i < cantAngeles && !demonios.isEmpty()) {
            EspirituAngelical angel = angeles.get(i);
            EspirituDemoniaco demonio = demonios.getFirst();
            angel.atacar(demonio);
            if(!demonio.estaConectado()){ // si se derrota un demonio, se lo saca de la lista
                demonios.removeFirst();
            }
            i++;
        }
    }

    public Espiritu invocarA(Espiritu espiritu) {
        if (espiritu.estaConectado()) throw new ExceptionEspirituOcupado(espiritu);
        if (this.getMana() < 10) return espiritu;

        espiritu.setUbicacion(this.getUbicacion());
        this.setMana(this.getMana() - 10);
        return espiritu;
    }

    public void desconectarEspiritu(EspirituDemoniaco espirituDemoniaco){
        this.espiritus.remove(espirituDemoniaco);
    }
}