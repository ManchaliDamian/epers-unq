package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.InvocacionNoPermitidaException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Getter @Setter @NoArgsConstructor @ToString
@Entity
@DiscriminatorValue("SANTUARIO")
public class Santuario extends Ubicacion {

    public Santuario(String nombre, Integer flujoDeEnergia)  {
        super(nombre, flujoDeEnergia);
    }

    @Override
    protected double getMultiplicadorMana() {
        return 1.5;
    }

    @Override
    public void invocarAngel(Espiritu espiritu) {
        this.moverAngel(espiritu);
    }

    @Override
    public void invocarDemonio(Espiritu espiritu) {
        throw new InvocacionNoPermitidaException(espiritu, this);
    }

    @Override
    public void moverAngel(Espiritu espiritu) {
        espiritu.setUbicacion(this);
    }

    @Override
    public void moverDemonio(Espiritu espiritu) {
        espiritu.setUbicacion(this);
        espiritu.perderNivelDeConexion(10);
    }

    @Override
    public void recuperarConexionComoAngel(Espiritu espiritu) {
        espiritu.aumentarNivelDeConexion(this.getFlujoDeEnergia());
    }

    @Override
    public TipoUbicacion getTipo() {
        return TipoUbicacion.SANTUARIO;
    }

}
