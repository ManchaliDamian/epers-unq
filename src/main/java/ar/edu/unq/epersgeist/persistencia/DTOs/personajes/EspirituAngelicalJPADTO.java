package ar.edu.unq.epersgeist.persistencia.DTOs.personajes;

import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.UbicacionJPADTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@DiscriminatorValue("ANGELICAL")
@Entity(name = "EspirituAngelical")
public class EspirituAngelicalJPADTO extends EspirituJPADTO {

    public EspirituAngelicalJPADTO(String nombre, UbicacionJPADTO ubicacion) {
        super( nombre, ubicacion, TipoEspiritu.ANGELICAL);
    }
}