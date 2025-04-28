package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;

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
    @Check(constraints = "mana BETWEEN 0 AND mana_max")
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
        if (noEsMismaUbicacion(espiritu)) {
            throw new EspirituNoEstaEnLaMismaUbicacionException(espiritu,this);
        } else if (espiritu.estaConectado()) {
            throw new ConectarException(espiritu,this);
        }
        espiritus.add(espiritu);
        espiritu.conectarA(this);

    }

    private boolean noEsMismaUbicacion(Espiritu espiritu) {
        return !this.ubicacion.equals(espiritu.getUbicacion());
    }

    public void desvincularseDe(Espiritu espiritu) {
        if (espiritus.remove(espiritu)) {
            espiritu.setMediumConectado(null);
        }
    }

    public void exorcizarA(List<EspirituAngelical> angeles, List<EspirituDemoniaco> demonios){
        int i = 0;
        if (angeles.isEmpty()){
            throw new ExorcistaSinAngelesException(this);
        }
        while (i < angeles.size() && !demonios.isEmpty()) {
            EspirituAngelical angel = angeles.get(i);
            EspirituDemoniaco demonio = demonios.getFirst();
            angel.atacar(demonio);
            if (!demonio.estaConectado()) {
                demonios.removeFirst();
            }
            i++;
        }
    }

    public void descansar() {
        this.recuperarMana(ubicacion.getCantidadRecuperada());
        espiritus.forEach(e -> e.recuperarConexionEn(ubicacion));
    }

    public void recuperarMana(int cantidad) {
        this.mana = Math.min(mana + cantidad, manaMax);
    }

    public Espiritu invocarA(Espiritu espiritu) {
        this.validarInvocar(espiritu);

        if (this.getMana() >= 10) {
            espiritu.serInvocadoEn(this.ubicacion);
            this.mana -= 10;
        }

        return espiritu;
    }

    public void validarInvocar(Espiritu espiritu){
        if (espiritu.estaConectado()) throw new ExceptionEspirituOcupado(espiritu);
    }

    public void mover(Ubicacion ubicacion) {
        this.setUbicacion(ubicacion);
        this.espiritus.forEach(e -> e.mover(ubicacion));
    }
}