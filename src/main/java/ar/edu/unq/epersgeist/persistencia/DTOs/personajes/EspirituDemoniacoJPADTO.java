package ar.edu.unq.epersgeist.persistencia.DTOs.personajes;

import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity(name = "EspirituDemoniaco")
@DiscriminatorValue("DEMONIACO")
public class EspirituDemoniacoJPADTO extends EspirituJPADTO {

    public EspirituDemoniacoJPADTO(String nombre, UbicacionJPADTO ubicacion) {
        super( nombre, ubicacion, TipoEspiritu.DEMONIACO);
    }
}