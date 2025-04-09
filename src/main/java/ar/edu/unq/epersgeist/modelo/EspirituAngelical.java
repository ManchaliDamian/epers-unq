package ar.edu.unq.epersgeist.modelo;
import jakarta.persistence.*;
import lombok.*;

import java.util.Random;

import static java.lang.Math.min;
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("ANGELICAL")
public class EspirituAngelical extends Espiritu{

    public EspirituAngelical(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion, nombre, ubicacion);
        this.setTipo(TipoEspiritu.ANGELICAL);
    }
    public void atacar(EspirituDemoniaco objetivo){
        Random random = new Random();
        int probAtaqueExitoso = this.probabilidadDeAtaqueExitoso();
        int defensaDemonio = random.nextInt(1,100);

        if(probAtaqueExitoso > defensaDemonio){
            //Ataque exitoso.
            int cantidad = this.getNivelDeConexion() / 2;
            //El demoniaco pierde nivelDeConexion.
            objetivo.recibirAtaque(cantidad);
        }else{
            //Ataque fallido, el angelical pierde 5 nivel de conexión
           this.perderNivelDeConexion(5);
        }
    }

    protected int probabilidadDeAtaqueExitoso(){
        Random random = new Random();
        int cantidad = random.nextInt(10) + 1;
        int cantAtaque = cantidad + this.getNivelDeConexion();
        return min(cantAtaque,100);
    }

}
