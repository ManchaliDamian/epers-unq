package ar.edu.unq.epersgeist.modelo;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.*;

@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

@Entity
public abstract class Espiritu {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "nivel_de_conexion BETWEEN 0 AND 100")
    protected Integer nivelDeConexion;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "medium_id")
    private Medium mediumConectado;

    public Espiritu (@NonNull String nombre, @NonNull Ubicacion ubicacion) {

        this.nivelDeConexion = 0;
        this.nombre = nombre;
        this.ubicacion = ubicacion;;
    }

    public void conectarA(Medium medium){
        this.setMediumConectado(medium);
        this.aumentarConexion(medium);
    }

    private void aumentarConexion(Medium medium) {
        int aumento = (int) Math.round(medium.getMana() * 0.20);

        this.setNivelDeConexion(
                Math.min(this.getNivelDeConexion() + aumento, 100)
        );
    }

    protected void perderNivelDeConexion(int cantidad){
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        int nuevoNivel = this.getNivelDeConexion() - cantidad;
        this.setNivelDeConexion(Math.max(nuevoNivel, 0));

        if (this.getNivelDeConexion() == 0 && this.estaConectado()) {
            this.getMediumConectado().desvincularseDe(this);
        }
    }

    public boolean estaConectado() {
        return this.getMediumConectado() != null;
    }

    public void descansar(Ubicacion ubicacion) {
        ubicacion.aplicarEfectoEspiritu(this);
    }

    public boolean puedeSerInvocadoEnCementerio() {return false;}

    public boolean puedeSerInvocadoEnSantuario() {return false;}

    public void recibirEfectoDe(Cementerio cementerio){};

    public void recibirEfectoDe(Santuario santuario){};
}
