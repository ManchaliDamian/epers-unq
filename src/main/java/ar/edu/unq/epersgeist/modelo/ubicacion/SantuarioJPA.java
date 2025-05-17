package ar.edu.unq.epersgeist.modelo.ubicacion;

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
@DiscriminatorValue("SANTUARIO")
public class SantuarioJPA extends UbicacionJPA {

    public SantuarioJPA(String nombre, Integer flujoDeEnergia)  {
        super(nombre, flujoDeEnergia, TipoUbicacion.SANTUARIO);
    }
}
