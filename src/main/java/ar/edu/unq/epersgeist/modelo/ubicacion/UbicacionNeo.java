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
    @GeneratedValue
    private Long id;

    private String nombre;
    private Integer flujoDeEnergia;

    private TipoUbicacion tipo;

    @Relationship(type = "CONECTA", direction = Relationship.Direction.OUTGOING)
    private Set<UbicacionNeo> conexiones = new HashSet<>();

    public UbicacionNeo(Long id, String nombre, Integer flujoDeEnergia, TipoUbicacion tipo) {
        this.id = id;
        this.nombre = nombre;
        this.flujoDeEnergia = flujoDeEnergia;
        this.tipo=tipo;
    }
}
