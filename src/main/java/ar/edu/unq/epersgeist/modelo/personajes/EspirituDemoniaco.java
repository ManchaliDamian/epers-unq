package ar.edu.unq.epersgeist.modelo.personajes;

import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import lombok.*;

@Getter
@Setter
@ToString
public class EspirituDemoniaco extends Espiritu{

    protected EspirituDemoniaco() {
        super(TipoEspiritu.DEMONIACO);
    }

    public EspirituDemoniaco(String nombre, Ubicacion ubicacion) {
        super(nombre, ubicacion, TipoEspiritu.DEMONIACO, 10, 5);
    }

    public EspirituDemoniaco(String nombre, Ubicacion ubicacion, Integer ataque, Integer defensa) {
        super(nombre, ubicacion, TipoEspiritu.DEMONIACO, ataque + 10, defensa + 5);
    }

    @Override
    public void serInvocadoEn(Ubicacion ubicacion) {
        ubicacion.invocarDemonio(this);
    }

    @Override
    public void recuperarConexionEn(Ubicacion ubicacion) {
        ubicacion.recuperarConexionComoDemonio(this);
    }

    @Override
    protected void mover(Ubicacion ubicacion) {
        ubicacion.moverDemonio(this);
    }

}
