package ar.edu.unq.epersgeist.modelo;

public class EspirituDemoniaco extends Espiritu{

    public EspirituDemoniaco(Integer nivelDeConexion, String nombre, Ubicacion ubicacion){
        super(nivelDeConexion,nombre, ubicacion);
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

    //@Override
    //public boolean puedeExorcizar(){return false;}
}
