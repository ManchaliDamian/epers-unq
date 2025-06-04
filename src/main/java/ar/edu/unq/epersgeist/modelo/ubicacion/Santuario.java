package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.exception.InvocacionNoPermitidaException;

import lombok.*;

@Getter @Setter @NoArgsConstructor @ToString
public class Santuario extends Ubicacion {

    public Santuario( String nombre, Integer flujoDeEnergia, Poligono poligono)  {
        super(nombre, flujoDeEnergia, TipoUbicacion.SANTUARIO, poligono);

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

}
