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
    public void aplicarEfectoMedium(Medium medium) {
        int recuperacion = (int) (getFlujoDeEnergia() * 0.5);
        medium.recuperarMana(recuperacion);
    }

    @Override
    public  void aplicarEfectoEspiritu(Espiritu espiritu){
        espiritu.recibirEfectoDe(this);
    }
}
