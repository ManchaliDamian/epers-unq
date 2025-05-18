package ar.edu.unq.epersgeist.modelo.personajes;
import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.generador.Generador;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("ANGELICAL")
public class EspirituAngelical extends Espiritu{

    public EspirituAngelical(String nombre, Ubicacion ubicacion) {
        super( nombre, ubicacion, TipoEspiritu.ANGELICAL);
    }

    @Override
    public void atacar(Espiritu objetivo) {
        int probAtaqueExitoso = this.probabilidadDeAtaqueExitoso();
        int defensaDemonio = Generador.entre(1, 100);

        if (probAtaqueExitoso > defensaDemonio) {
            int cantidad = this.getNivelDeConexion() / 2;
            objetivo.perderNivelDeConexion(cantidad);
        } else {
            this.perderNivelDeConexion(5);
        }
    }

    private int probabilidadDeAtaqueExitoso() {
        int cantidad = Generador.entre(1, 10);
        int cantAtaque = cantidad + this.getNivelDeConexion();
        return Math.min(cantAtaque, 100);
    }

    @Override
    public void serInvocadoEn(Ubicacion ubicacion) {
        ubicacion.invocarAngel(this);
    }

    @Override
    public void recuperarConexionEn(Ubicacion ubicacion) {
        ubicacion.recuperarConexionComoAngel(this);
    }

    @Override
    protected void mover(Ubicacion ubicacion) {
        ubicacion.moverAngel(this);
    }

}
