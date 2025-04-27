package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @ToString
@Entity
@DiscriminatorValue("CEMENTERIO")
public class Cementerio extends Ubicacion {

    public Cementerio( String nombre, Integer flujoDeEnergia) {
        super( nombre, flujoDeEnergia);
    }

    @Override
    public boolean permiteInvocar(Espiritu espiritu) {
        return espiritu.puedeSerInvocadoEnCementerio();
    }

    @Override
    public void aplicarEfectoEspiritu(Espiritu espiritu){
        espiritu.recibirEfectoDe(this);
    }

    @Override
    protected double getMultiplicadorMana() {
        return 0.5;
    }
}
