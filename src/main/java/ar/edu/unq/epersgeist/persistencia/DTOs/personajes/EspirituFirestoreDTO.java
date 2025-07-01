package ar.edu.unq.epersgeist.persistencia.DTOs.personajes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EspirituFirestoreDTO {
    private Long idSQL;
    private String nombre;
    private int vida;
    private int ataque;
    private int defensa;
    private int batallasGanadas;
    private int batallasPerdidas;
    private int batallasJugadas;
}
