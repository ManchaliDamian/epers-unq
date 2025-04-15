package ar.edu.unq.epersgeist.modelo;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("ANGELICAL")
public class EspirituAngelical extends Espiritu{

    public EspirituAngelical( String nombre, Ubicacion ubicacion, GeneradorDeNumeros generador) {
        super( nombre, ubicacion, generador);
        this.setTipo(TipoEspiritu.ANGELICAL);

    }

    public void atacar(EspirituDemoniaco objetivo) {
        int probAtaqueExitoso = this.probabilidadDeAtaqueExitoso();
        int defensaDemonio = generador.entre(1, 100); // reemplaza Random

        if (probAtaqueExitoso > defensaDemonio) {
            int cantidad = this.getNivelDeConexion() / 2;
            objetivo.recibirAtaque(cantidad);
        } else {
            this.perderNivelDeConexion(5);
        }
    }

    protected int probabilidadDeAtaqueExitoso() {
        int cantidad = generador.entre(1, 10); // reemplaza Random
        int cantAtaque = cantidad + this.getNivelDeConexion();
        return Math.min(cantAtaque, 100);
    }

}
