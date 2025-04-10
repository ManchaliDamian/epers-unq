package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.ConectarException;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEstaEnLaMismaUbicacionException;
import ar.edu.unq.epersgeist.modelo.exception.NivelDeConexionException;
import ar.edu.unq.epersgeist.modelo.exception.ExceptionEspirituOcupado;

import jakarta.persistence.*;
import org.hibernate.annotations.*;

import java.util.Objects;

import static java.lang.Math.max;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Espiritu {

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

    @Column(nullable = false)
    private TipoEspiritu tipo;

    @ManyToOne
    @JoinColumn(name = "medium_id")
    private Medium mediumConectado;

    public Espiritu() {
        // Constructor vacío requerido por JPA
    }

    public Espiritu(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        validarNivelDeConexion(nivelDeConexion);
        this.nivelDeConexion = nivelDeConexion;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Integer getNivelDeConexion() {
        return nivelDeConexion;
    }

    public void setNivelDeConexion(Integer nivelDeConexion) {
        this.nivelDeConexion = nivelDeConexion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoEspiritu getTipo() {
        return tipo;
    }

    public void setTipo(TipoEspiritu tipo) {
        this.tipo = tipo;
    }

    public Medium getMediumConectado() {
        return mediumConectado;
    }

    public void setMediumConectado(Medium mediumConectado) {
        this.mediumConectado = mediumConectado;
    }

    public void conexionEnAumento(Medium medium) {
        this.estaEnLaMismaUbicacion(medium);
        this.aumentarConexion(medium);
    }

    public void estaEnLaMismaUbicacion(Medium medium) {
        if (!this.esMismaUbicacion(medium)) {
            throw new EspirituNoEstaEnLaMismaUbicacionException(this, medium);
        }
    }

    public boolean esMismaUbicacion(Medium medium) {
        return this.getUbicacion().equals(medium.getUbicacion());
    }

    public void aumentarConexion(Medium medium) {
        if (this.getMediumConectado() != medium) {
            throw new ConectarException(this, medium);
        }
        int aumento = (int) Math.round(medium.getMana() * 0.20);
        this.setNivelDeConexion(
                Math.min(this.getNivelDeConexion() + aumento, 100)
        );
    }

    public void validarNivelDeConexion(Integer nivelDeConexion) {
        if (nivelDeConexion < 0 || nivelDeConexion > 100) {
            throw new NivelDeConexionException();
        }
    }

    public void validarDisponibilidad() {
        if (estaConectado()) {
            throw new ExceptionEspirituOcupado(this);
        }
    }

    protected void perderNivelDeConexion(int cantidad) {
        int nivelDeConexionResultante = this.getNivelDeConexion() - cantidad;
        if (nivelDeConexionResultante <= 0) {
            this.getMediumConectado().desvincularseDe(this);
        } else {
            this.setNivelDeConexion(nivelDeConexionResultante);
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

    public void desvincularse() {
        this.setMediumConectado(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Espiritu)) return false;
        Espiritu espiritu = (Espiritu) o;
        return Objects.equals(id, espiritu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Espiritu{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", nivelDeConexion=" + nivelDeConexion +
                ", ubicacion=" + ubicacion +
                '}';
    }
}
