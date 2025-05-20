package ar.edu.unq.epersgeist.persistencia.EspirituJPA;

import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.generador.Generador;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.UbicacionJPA.UbicacionJPA;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("ANGELICAL")
public class EspirituAngelicalJPA extends EspirituJPA {

    public EspirituAngelicalJPA(String nombre, UbicacionJPA ubicacion) {
        super( nombre, ubicacion, TipoEspiritu.ANGELICAL);
    }


}
