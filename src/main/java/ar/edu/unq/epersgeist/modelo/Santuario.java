package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
@Entity
@DiscriminatorValue("SANTUARIO")
public class Santuario extends Ubicacion {

    public Santuario( String nombre, Integer flujoDeEnergia) {
        super(nombre, flujoDeEnergia);
    }

    @Override
    public boolean permiteInvocar(Espiritu espiritu) {
        return espiritu.puedeSerInvocadoEnSantuario();
    }

    @Override
    public void aplicarEfectoEspiritu(Espiritu espiritu){
        espiritu.recibirEfectoDe(this);
    }

    @Override
    protected double getMultiplicadorMana() {
        return 1.5;
    }
}
