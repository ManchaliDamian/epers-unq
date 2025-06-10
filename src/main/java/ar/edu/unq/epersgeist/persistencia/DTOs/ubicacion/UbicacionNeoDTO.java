package ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion;

import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@ToString
@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Node
public class UbicacionNeoDTO {
    @EqualsAndHashCode.Include
    @Id
    private Long id;

    private TipoUbicacion tipo;

    @Relationship(type = "CONECTA", direction = Relationship.Direction.OUTGOING)
    private Set<UbicacionNeoDTO> conexiones = new HashSet<>();

    public UbicacionNeoDTO(Long id , TipoUbicacion tipo) {
        this.id = id;
        this.tipo=tipo;
    }
}
