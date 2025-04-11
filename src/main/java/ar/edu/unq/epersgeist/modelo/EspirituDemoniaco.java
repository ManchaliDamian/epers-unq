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

    public EspirituDemoniaco(Integer nivelDeConexion, String nombre, Ubicacion ubicacion, GeneradorDeNumeros generador) {
        super(nivelDeConexion, nombre, ubicacion, generador);
        this.setTipo(TipoEspiritu.DEMONIACO);
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
        this.getMediumConectado().desconectarEspiritu(this);
        this.setNivelDeConexion(0);
        this.setMediumConectado(null);
    }

}
