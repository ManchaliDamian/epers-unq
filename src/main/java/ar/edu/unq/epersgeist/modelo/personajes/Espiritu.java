package ar.edu.unq.epersgeist.modelo.personajes;
import ar.edu.unq.epersgeist.exception.Conflict.EspirituNoDominableException;
import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.exception.Conflict.EspirituDominadoException;
import ar.edu.unq.epersgeist.modelo.generador.Generador;
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
    protected Integer nivelDeConexion = 0;
    @EqualsAndHashCode.Include
    private String nombre;
    private Medium mediumConectado;
    private TipoEspiritu tipo;

    private Espiritu dominador;

    private Integer vida = 100;
    private Integer defensa = 5;
    private Integer ataque = 5;
    private Integer batallasGanadas = 0;
    private Integer batallasPerdidas = 0;
    private Integer batallasJugadas = 0;

    //auditoria
    private Date createdAt;
    private Date updatedAt;
    private boolean deleted = false;

    public Espiritu (@NotBlank String nombre, @NonNull Ubicacion ubicacion, @NonNull TipoEspiritu tipo) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.tipo = tipo;
    }

    public Espiritu(
            @NotBlank String nombre,
            @NonNull Ubicacion ubicacion,
            @NonNull TipoEspiritu tipo,
            @NonNull Integer ataque,
            @NonNull Integer defensa
    ) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.tipo = tipo;
        this.ataque = ataque;
        this.defensa = defensa;
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

    public void perderVida(int cantidad){
        int nuevaVida = this.getVida() - cantidad;
        this.setVida(Math.max(nuevaVida, 0));
    }

    public void combatir(Espiritu espirituACombatir){
        this.participarEnBatalla();
        espirituACombatir.participarEnBatalla();

        if (this.getAtaque() > espirituACombatir.getDefensa()){
            this.registrarVictoria();
            espirituACombatir.recibirImpacto(this.getAtaque());
            espirituACombatir.registrarDerrota();
        }else{
            this.recibirImpacto(espirituACombatir.getDefensa() / 2);
            this.registrarDerrota();
            espirituACombatir.registrarVictoria();
        }
    }

    private void recibirImpacto(Integer ataqueEntrante) {
        int dmg = calcularDanio(ataqueEntrante);
        this.perderVida(dmg);
    }

    private int calcularDanio(int ataque) {
        int dmg = ataque - this.getDefensa();
        return Math.min(Math.max(dmg, 0), 100); //  0 <= daño <= 100
    }

    private void participarEnBatalla() {
        this.batallasJugadas++;
    }

    public void registrarVictoria() {
        this.batallasGanadas++;
    }

    public void registrarDerrota() {
        this.batallasPerdidas++;
    }

    public void atacar(Espiritu objetivo){};
    public abstract void serInvocadoEn(Ubicacion ubicacion);
    public abstract void recuperarConexionEn(Ubicacion ubicacion);

    protected abstract void mover(Ubicacion nuevaUbicacion);

}
