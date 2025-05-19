package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@ToString
@Setter @Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Node("Ubicacion")
public class UbicacionNeo {

    @Id
    private Long id;

    private TipoUbicacion tipo;
    private boolean deleted = false;

    @Relationship(type = "CONECTA", direction = Relationship.Direction.OUTGOING)
    private Set<UbicacionNeo> conexiones = new HashSet<>();

    public UbicacionNeo( Long id ,TipoUbicacion tipo) {
        this.id = id;
        this.tipo=tipo;
    }
}
