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

    private Long id;
    public EspirituAngelical(Integer nivelDeConexion, String nombre, Ubicacion ubicacion) {
        super(nivelDeConexion, nombre, ubicacion);
        this.setTipo(TipoEspiritu.ANGELICAL);
    }
    public void atacar(EspirituDemoniaco objetivo){
        int cantidadAtaqueExitoso = this.calcularAtaque();

        if(cantidadAtaqueExitoso > objetivo.getNivelDeConexion()){
            //Ataque exitoso.
            int cantidad = this.getNivelDeConexion() / 2;
            //El demoniaco pierde nivelDeConexion.
            objetivo.recibirAtaque(cantidad);
        }else{
            //Ataque fallido, el angelical pierde 5 nivel de conexión
           this.perderNivelDeConexion(5);
        }
    }

    protected int calcularAtaque(){
        Random random = new Random();
        int cantidad = random.nextInt(10) + 1;
        int cantAtaque = cantidad + this.getNivelDeConexion();
        return min(cantAtaque,100);
    }

    //@Override
    //public boolean puedeExorcizar(){return true;}

}
