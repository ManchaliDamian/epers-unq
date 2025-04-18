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
        this.setTipo(TipoEspiritu.DEMONIACO);
    }

    public void recibirAtaque(int cantidad){
        this.perderNivelDeConexion(cantidad);
    }

}
