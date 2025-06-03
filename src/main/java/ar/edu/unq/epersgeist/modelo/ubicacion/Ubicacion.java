package ar.edu.unq.epersgeist.modelo.ubicacion;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import lombok.*;

@ToString
@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Ubicacion {
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private String nombre;
    private Integer flujoDeEnergia;
    private TipoUbicacion tipo;

    //auditoria
    private Date createdAt;
    private Date updatedAt;
    private boolean deleted = false;

    private List<Coordenada> coordenadas;

    private Set<Ubicacion> conexiones = new HashSet<>();

    public Ubicacion(String nombre, Integer flujoDeEnergia, TipoUbicacion tipo, @NonNull List<Coordenada> coordenadas) {
        if (flujoDeEnergia < 0 || flujoDeEnergia > 100) {
            throw new IllegalArgumentException("El flujo de energía debe estar entre 0 y 100");
        }
        this.nombre = nombre;
        this.flujoDeEnergia = flujoDeEnergia;
        this.tipo = tipo;
        this.coordenadas = coordenadas;
    }

    public void cambiarNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
    }

    public Integer getCantidadRecuperada() {
        return (int) (this.flujoDeEnergia * getMultiplicadorMana());
    }

    protected abstract double getMultiplicadorMana();

    public abstract void invocarAngel(Espiritu espiritu);
    public abstract void invocarDemonio(Espiritu espiritu);

    public void recuperarConexionComoAngel(Espiritu espiritu) { }
    public void recuperarConexionComoDemonio(Espiritu espiritu) { }

    public abstract void moverAngel(Espiritu espiritu);
    public abstract void moverDemonio(Espiritu espiritu);

}