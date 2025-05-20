package ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion;

import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor
@ToString
@Entity(name = "Santuario")
@DiscriminatorValue("SANTUARIO")
public class SantuarioJPADTO extends UbicacionJPADTO {

    public SantuarioJPADTO(String nombre, Integer flujoDeEnergia)  {
        super(nombre, flujoDeEnergia, TipoUbicacion.SANTUARIO);
    }
}
