package ar.edu.unq.epersgeist.modelo.personajes;

import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Ubicacion;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @ToString
@Entity
@DiscriminatorValue("DEMONIACO")
public class EspirituDemoniaco extends Espiritu{

    public EspirituDemoniaco( String nombre, Ubicacion ubicacion) {
        super(nombre, ubicacion);
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
    
    public TipoEspiritu getTipo(){
        return TipoEspiritu.DEMONIACO;
    }
}
