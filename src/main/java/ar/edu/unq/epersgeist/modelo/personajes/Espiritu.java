package ar.edu.unq.epersgeist.modelo.personajes;

import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import ar.edu.unq.epersgeist.persistencia.UbicacionJPA.UbicacionJPA;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.*;

import java.util.Date;

@Getter @Setter @NoArgsConstructor(force = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public abstract class Espiritu {

    private Long id;

    private Ubicacion ubicacion;

    protected Integer nivelDeConexion;


    private String nombre;

    private Medium mediumConectado;

    private final TipoEspiritu tipo;

    private Date createdAt;


    private Date updatedAt;


    private boolean deleted = false;

    public Espiritu (String nombre, Ubicacion ubicacion, TipoEspiritu tipo) {

        this.nivelDeConexion = 0;
        this.nombre = nombre;

        this.ubicacion = ubicacion;
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
