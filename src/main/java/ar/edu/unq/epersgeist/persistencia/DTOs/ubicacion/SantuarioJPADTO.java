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
@DiscriminatorValue("SANTUARIO")
@Entity(name = "Santuario")
public class SantuarioJPADTO extends UbicacionJPADTO {

    public SantuarioJPADTO(String nombre, Integer flujoDeEnergia)  {
        super(nombre, flujoDeEnergia, TipoUbicacion.SANTUARIO);
    }
}
