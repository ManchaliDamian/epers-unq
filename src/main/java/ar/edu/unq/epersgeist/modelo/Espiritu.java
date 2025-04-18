package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.modelo.exception.NivelDeConexionException;
import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituOcupado;

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
    @Check(constraints = "nivelDeConexion BETWEEN 0 AND 100")
    private Integer nivelDeConexion;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "medium_id")
    private Medium mediumConectado;

    public Espiritu ( @NonNull String nombre, @NonNull Ubicacion ubicacion) {

        this.nivelDeConexion = 0;
        this.nombre = nombre;
        this.ubicacion = ubicacion;;
    }

    public void conectarA(Medium medium){
        this.setMediumConectado(medium);
        this.aumentarConexion(medium);
    }
    //DONDE SE USA ESTO?---------------------------------------------------------
    public void estaEnLaMismaUbicacion(Medium medium){
        if(!this.esMismaUbicacion(medium)){
            throw new EspirituNoEstaEnLaMismaUbicacionException(this,medium);
        }
    }
    //--------------------------------------------------------------------------
    public boolean esMismaUbicacion(Medium medium) {
        return this.getUbicacion().equals(medium.getUbicacion());
    }

    public void aumentarConexion(Medium medium) {
        //este if nunca pasaria si primero se lo seteamos en conectarA
        if (this.getMediumConectado() != medium){
            throw new ConectarException(this, medium);
        }
        //--------------------------------------------------------------
        int aumento = (int) Math.round(medium.getMana() * 0.20);

        this.setNivelDeConexion(
                Math.min(this.getNivelDeConexion() + aumento, 100)
        );
    }
    //esto solo se usa en los test--------------------------------------------
    public void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < 0 || nivelDeConexion > 100) {
            throw new NivelDeConexionException();
        }
    }

    public void validarDisponibilidad(){
        if(estaConectado()){
            throw new ExceptionEspirituOcupado(this);
        }
    }
    /// ----------------------------------------------------------------------

    protected void perderNivelDeConexion(int cantidad){
        int nuevoNivel = this.getNivelDeConexion() - cantidad;
        this.setNivelDeConexion(Math.max(nuevoNivel, 0));  // Asegura que no sea negativo

        if (this.getNivelDeConexion() == 0 && this.estaConectado()) {  // Verifica estado actual
            this.getMediumConectado().desvincularseDe(this);
        }
    }

    public boolean estaConectado() {
        return this.getMediumConectado() != null;
    }

    public void descansar() {
        this.setNivelDeConexion(
                Math.min(this.getNivelDeConexion() + 5, 100)
        );
    }

}
