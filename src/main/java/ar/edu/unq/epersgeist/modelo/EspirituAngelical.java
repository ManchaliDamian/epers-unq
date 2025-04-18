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

    public EspirituAngelical( String nombre, Ubicacion ubicacion) {
        super( nombre, ubicacion);

    }

    public void atacar(EspirituDemoniaco objetivo) {
        int probAtaqueExitoso = this.probabilidadDeAtaqueExitoso();
        int defensaDemonio = Generador.entre(1, 100);

        if (probAtaqueExitoso > defensaDemonio) {
            int cantidad = this.getNivelDeConexion() / 2;
            objetivo.recibirAtaque(cantidad);
        } else {
            this.perderNivelDeConexion(5);
        }
    }

    protected int probabilidadDeAtaqueExitoso() {
        int cantidad = Generador.entre(1, 10);
        int cantAtaque = cantidad + this.getNivelDeConexion();
        return Math.min(cantAtaque, 100);
    }

}
