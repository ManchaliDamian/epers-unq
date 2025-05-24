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
@DiscriminatorValue("CEMENTERIO")
@Entity(name = "Cementerio")
public class CementerioJPADTO extends UbicacionJPADTO {

    public CementerioJPADTO(String nombre, Integer flujoDeEnergia) {
        super(nombre, flujoDeEnergia, TipoUbicacion.CEMENTERIO);
    }
}
