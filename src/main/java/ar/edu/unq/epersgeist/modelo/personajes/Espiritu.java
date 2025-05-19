package ar.edu.unq.epersgeist.modelo.personajes;

import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import ar.edu.unq.epersgeist.modelo.ubicacion.UbicacionJPA;
import ar.edu.unq.epersgeist.mapper.UbicacionMapper;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Getter @Setter @NoArgsConstructor(force = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

@Entity
public abstract class Espiritu {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private UbicacionJPA ubicacion;

    @Transient
    private Ubicacion ubicacionModelo;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Check(constraints = "nivel_de_conexion BETWEEN 0 AND 100")
    protected Integer nivelDeConexion;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "medium_id")
    private Medium mediumConectado;

    private final TipoEspiritu tipo;

    //auditoria
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @Column(nullable = false)
    private boolean deleted = false;

    public Espiritu (@NonNull String nombre, @NonNull Ubicacion ubicacion, @NonNull TipoEspiritu tipo) {

        this.nivelDeConexion = 0;
        this.nombre = nombre;

        this.ubicacionModelo = ubicacion;
        this.tipo = tipo;


    }

    public void conectarA(Medium medium){
        this.setMediumConectado(medium);
        this.aumentarConexionCon(medium);
    }

    private void aumentarConexionCon(Medium medium) {
        int aumento = (int) Math.round(medium.getMana() * 0.20);

        this.aumentarNivelDeConexion(aumento);
    }

    public void perderNivelDeConexion(int cantidad){
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
        this.recuperarConexionEn(ubicacion);
    }

    public void aumentarNivelDeConexion(int aumento){
        this.nivelDeConexion = Math.min(this.getNivelDeConexion() + aumento, 100);
    }

    public void atacar(Espiritu objetivo){};
    public abstract void serInvocadoEn(Ubicacion ubicacion);
    public abstract void recuperarConexionEn(Ubicacion ubicacion);

    protected abstract void mover(Ubicacion nuevaUbicacion);

}
