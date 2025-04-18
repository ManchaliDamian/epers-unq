package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("DEMONIACO")
public class EspirituDemoniaco extends Espiritu{

    public EspirituDemoniaco( String nombre, Ubicacion ubicacion ) {
        super(nombre, ubicacion);

    }

    public void recibirAtaque(int cantidad){
        int cantidadPerdida = this.getNivelDeConexion() - cantidad;
        this.setNivelDeConexion(cantidadPerdida);
        this.evaluarDesconectarDemoniaco();
    }

    public void evaluarDesconectarDemoniaco(){
        if(this.getNivelDeConexion() <= 0){
            this.desconectarDelMedium();
        }
    }

    public void desconectarDelMedium(){
        this.getMediumConectado().desvincularseDe(this);
        this.setNivelDeConexion(0);

    }

}
