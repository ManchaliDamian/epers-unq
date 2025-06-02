package ar.edu.unq.epersgeist.modelo.personajes;

import ar.edu.unq.epersgeist.modelo.exception.*;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import lombok.*;


import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Medium {
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private String nombre;
    private Ubicacion ubicacion;
    private Integer manaMax;
    private Integer mana;
    private final Set<Espiritu> espiritus = new HashSet<>();
    private Coordenada coordenada;

    //auditoria
    private Date createdAt;
    private Date updatedAt;
    private boolean deleted = false;

    public Medium(String nombre, Integer manaMax, Integer mana, Ubicacion ubicacion, @NonNull Coordenada coordenada) {
        if (manaMax < 0) {
            throw new IllegalArgumentException("manaMax no puede ser negativo.");
        }
        if (mana < 0 || mana > manaMax) {
            throw new IllegalArgumentException("mana debe estar entre 0 y manaMax.");
        }
        this.nombre = nombre;
        this.manaMax = manaMax;
        this.mana = mana;
        this.ubicacion = ubicacion;
        this.coordenada = coordenada;
    }

    public void conectarseAEspiritu(Espiritu espiritu) {
        if (noEsMismaUbicacion(espiritu)) {
            throw new EspirituNoEstaEnLaMismaUbicacionException(espiritu,this);
        } else if (espiritu.estaConectado()) {
            throw new ConectarException(espiritu,this);
        }
        espiritus.add(espiritu);
        espiritu.conectarA(this);

    }

    private boolean noEsMismaUbicacion(Espiritu espiritu) {
        return !this.ubicacion.equals(espiritu.getUbicacion());
    }

    public void desvincularseDe(Espiritu espiritu) {
        if (espiritus.remove(espiritu)) {
            espiritu.setMediumConectado(null);
        }
    }
    public boolean esMismaUbicacionParaExorcizar(Ubicacion ubicacion){
        if (this.getUbicacion() == null) {
            return ubicacion == null;
        }
        return this.getUbicacion().equals(ubicacion);

    }
    public void exorcizarA(List<EspirituAngelical> angeles, List<EspirituDemoniaco> demonios, Ubicacion ubicacion){

        if (angeles.isEmpty()) {
            throw new ExorcistaSinAngelesException(this);
        }

        if (!esMismaUbicacionParaExorcizar(ubicacion)) {
            throw new ExorcizarNoPermitidoNoEsMismaUbicacion(ubicacion, this);
        }

        int i = 0;
        while (i < angeles.size() && !demonios.isEmpty()) {
            EspirituAngelical angel = angeles.get(i);
            EspirituDemoniaco demonio = demonios.getFirst();
            angel.atacar(demonio);
            if (!demonio.estaConectado()) {
                demonios.removeFirst();
            }
            i++;
        }
    }

    public void descansar() {
        this.recuperarMana(ubicacion.getCantidadRecuperada());
        espiritus.forEach(e -> e.recuperarConexionEn(ubicacion));
    }

    public void recuperarMana(int cantidad) {
        this.mana = Math.min(mana + cantidad, manaMax);
    }

    public Espiritu invocarA(Espiritu espiritu) {
        this.validarInvocar(espiritu);

        if (this.getMana() >= 10) {
            espiritu.serInvocadoEn(this.ubicacion);
            this.mana -= 10;
        }

        return espiritu;
    }

    public void validarInvocar(Espiritu espiritu){
        if (espiritu.estaConectado()) throw new EspirituOcupadoException(espiritu);
    }

    public void mover(Ubicacion ubicacion) {
        this.setUbicacion(ubicacion);
        this.espiritus.forEach(e -> {
                e.mover(ubicacion);
        });


    }
}