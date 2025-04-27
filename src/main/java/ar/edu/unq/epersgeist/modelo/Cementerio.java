package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.exception.InvocacionNoPermitidaException;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @ToString
@Entity
@DiscriminatorValue("CEMENTERIO")
public class Cementerio extends Ubicacion {

    public Cementerio(@NonNull String nombre,@NonNull Integer energiaUbicacion) {
        super(nombre, energiaUbicacion);
    }

    @Override
    protected double getMultiplicadorMana() {
        return 0.5;
    }

    @Override
    public void invocarAngel(Espiritu espiritu){
        throw new InvocacionNoPermitidaException(espiritu, this);
    }

    @Override
    public void invocarDemonio(Espiritu espiritu) {
        this.moverDemonio(espiritu);
    }

    @Override
    public void moverAngel(Espiritu espiritu) {
        espiritu.setUbicacion(this);
        espiritu.perderNivelDeConexion(5);
    }

    @Override
    public void moverDemonio(Espiritu espiritu) {
        espiritu.setUbicacion(this);
    }

    @Override
    public void recuperarConexionComoDemonio(Espiritu espiritu) {
        espiritu.aumentarNivelDeConexion(this.getFlujoDeEnergia());
    }
}
