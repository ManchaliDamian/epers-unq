package ar.edu.unq.epersgeist.persistencia.EspirituJPA;

import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
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
@DiscriminatorValue("DEMONIACO")
public class EspirituDemoniacoJPA extends EspirituJPA {

    public EspirituDemoniacoJPA(String nombre, UbicacionJPA ubicacion) {
        super(nombre, ubicacion, TipoEspiritu.DEMONIACO);
    }


}
