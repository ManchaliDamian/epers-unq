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
    public void aplicarEfectoMedium(Medium medium) {
        int recuperacion = (int) (getFlujoDeEnergia() * 1.5);
        medium.recuperarMana(recuperacion);
    }
    @Override
    public  void aplicarEfectoEspiritu(Espiritu espiritu){
        espiritu.recibirEfectoDe(this);
    }
}
