package ar.edu.unq.epersgeist.modelo;

public class EspirituDemoniaco extends Espiritu{

    public EspirituDemoniaco(String tipo, Integer nivelDeConexion, String nombre){
        super(tipo,nivelDeConexion,nombre);
    }

    public void recibirAtaque(int cantidad){
        this.setNivelDeConexion(cantidad);
    }

    public void evaluarDesconecarDemoniaco(){
        if(this.getNivelDeConexion() <= 0){
            this.desconectarDelMedium();
        }
    }

    public void desconectarDelMedium(){
        // this.getMediumConectado().desconectarEspiritu(this);
        this.setNivelDeConexion(0);
        this.setMediumConectado(null);
    }

    @Override
    public boolean puedeExorcizar(){return false;}
}
