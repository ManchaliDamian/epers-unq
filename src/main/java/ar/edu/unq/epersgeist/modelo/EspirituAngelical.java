package ar.edu.unq.epersgeist.modelo;
import java.util.Random;

import static java.lang.Math.min;


public class EspirituAngelical extends Espiritu{

    public EspirituAngelical(String tipo, Integer nivelDeConexion, String nombre){
        super(tipo,nivelDeConexion,nombre);
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

    @Override
    public boolean puedeExorcizar(){return true;}

}
