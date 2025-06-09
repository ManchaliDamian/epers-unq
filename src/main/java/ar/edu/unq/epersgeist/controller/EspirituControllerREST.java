package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.espiritu.CreateEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.espiritu.UpdateEspirituDTO;
import ar.edu.unq.epersgeist.modelo.enums.TipoEspiritu;
import ar.edu.unq.epersgeist.modelo.enums.Direccion;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.exception.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/espiritu")
public final  class EspirituControllerREST {

    private final EspirituService espirituService;
    private final UbicacionService ubicacionService;

    public EspirituControllerREST(EspirituService espirituService, UbicacionService ubicacionService){
        this.espirituService = espirituService;
        this.ubicacionService = ubicacionService;
    }

    @PostMapping
    public ResponseEntity<EspirituDTO> createEspiritu(@Valid @RequestBody CreateEspirituDTO dto) {
        Ubicacion ubicacion = ubicacionService.recuperar(dto.ubicacionId())
                .orElseThrow(() -> new UbicacionNoEncontradaException(dto.ubicacionId()));

        Espiritu espiritu = dto.aModeloEspiritu(ubicacion);
        Coordenada coordenada = dto.aModeloCoordenada();
        Espiritu creado = espirituService.guardar(espiritu, coordenada);

        return ResponseEntity
                .created(URI.create("/espiritu/" + creado.getId()))
                .body(EspirituDTO.desdeModelo(creado));
    }

    @GetMapping
    public List<EspirituDTO> getEspiritus(@RequestParam(required = false) TipoEspiritu tipo){
        var espiritus = switch (tipo){
            case ANGELICAL -> espirituService.recuperarAngeles();
            case DEMONIACO -> espirituService.recuperarDemonios();
            case null -> espirituService.recuperarTodos();
        };
        return espiritus.stream().map(EspirituDTO::desdeModelo).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTO> getEspirituById(@PathVariable Long id){
        Espiritu espiritu = espirituService.recuperar(id).orElseThrow(() -> new EspirituNoEncontradoException(id));
        return ResponseEntity.ok(EspirituDTO.desdeModelo(espiritu));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEspiritu(@PathVariable Long id){
        espirituService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/demoniacos")
    public List<EspirituDTO> espiritusDemoniacos(@RequestParam Direccion direccion, @RequestParam int pagina, @RequestParam int cantidadPorPagina) {
        return espirituService.espiritusDemoniacos(direccion, pagina, cantidadPorPagina).stream()
                .map(EspirituDTO::desdeModelo)
                .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspirituDTO> updateById(@PathVariable Long id, @Valid @RequestBody UpdateEspirituDTO dto) {
        Espiritu espiritu = espirituService.recuperar(id).orElseThrow(() -> new EspirituNoEncontradoException(id));
        dto.actualizarModelo(espiritu);
        Espiritu guardado = espirituService.actualizar(espiritu);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(guardado));
    }

    @PutMapping("/{idEspiritu}/conectar/{idMedium}")
    public ResponseEntity<MediumDTO> conectarEspiritu(@PathVariable Long idEspiritu,@PathVariable Long idMedium){
        Medium medium = espirituService.conectar(idEspiritu,idMedium);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }
}
