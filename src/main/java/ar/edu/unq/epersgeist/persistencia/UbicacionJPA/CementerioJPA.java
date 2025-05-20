package ar.edu.unq.epersgeist.persistencia.UbicacionJPA;

import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("CEMENTERIO")
public class CementerioJPA extends UbicacionJPA {

    public CementerioJPA(String nombre, Integer flujoDeEnergia) {
        super(nombre, flujoDeEnergia, TipoUbicacion.CEMENTERIO);
    }
}
