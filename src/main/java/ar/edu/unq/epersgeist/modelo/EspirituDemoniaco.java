package ar.edu.unq.epersgeist.modelo;

import jakarta.persistence.Entity;
import lombok.*;

@Getter @Setter @NoArgsConstructor @ToString
@Entity
public class EspirituDemoniaco extends Espiritu{

    public EspirituDemoniaco(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion, nombre, ubicacion);
        this.setTipo(TipoEspiritu.DEMONIACO);
    }

    public void recibirAtaque(int cantidad){
        this.setNivelDeConexion(cantidad);
    }

    public void evaluarDesconectarDemoniaco(){
        if(this.getNivelDeConexion() <= 0){
            this.desconectarDelMedium();
        }
    }

    public void desconectarDelMedium(){
        // this.getMediumConectado().desconectarEspiritu(this);
        this.setNivelDeConexion(0);
        this.setMediumConectado(null);
    }

    //@Override
    //public boolean puedeExorcizar(){return false;}
}
