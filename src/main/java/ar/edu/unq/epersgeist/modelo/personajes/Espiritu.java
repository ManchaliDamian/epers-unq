package ar.edu.unq.epersgeist.modelo.personajes;
import ar.edu.unq.epersgeist.exception.Conflict.EspirituNoDominableException;
import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.exception.Conflict.EspirituDominadoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString

public abstract class Espiritu {
    @EqualsAndHashCode.Include
    private Long id;
    private Ubicacion ubicacion;
    protected Integer nivelDeConexion;
    @EqualsAndHashCode.Include
    private String nombre;
    private Medium mediumConectado;
    private TipoEspiritu tipo;

    private Espiritu dominador;

    //auditoria
    private Date createdAt;
    private Date updatedAt;
    private boolean deleted = false;

    public Espiritu (@NotBlank String nombre, @NonNull Ubicacion ubicacion, @NonNull TipoEspiritu tipo) {
        this.nivelDeConexion = 0;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.tipo = tipo;
    }

    protected Espiritu(@NonNull TipoEspiritu tipo) {
        this.tipo = tipo;
    }

    public void conectarA(Medium medium){
        if(estaDominado()){
            throw new EspirituDominadoException(this.getNombre());
        }
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

    public boolean estaDominado() {
        return this.getDominador() != null;
    }
    public boolean estaConectado() {
        return this.getMediumConectado() != null;
    }

    public Espiritu dominar(Espiritu espirituADominar) {

        if (this.getDominador() != null && this.getDominador().equals(espirituADominar)) {
            throw new EspirituNoDominableException(this.getId(), espirituADominar.getId());
        }
        if (!espirituADominar.estaDominado() && espirituADominar.nivelDeConexion < 50) {
            espirituADominar.setDominador(this);
        }

        return this;
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
