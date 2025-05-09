package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.CreateEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.UpdateEspirituDTO;
import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Espiritu espiritu = dto.aModelo(ubicacion);
        Espiritu creado = espirituService.guardar(espiritu);

        return ResponseEntity
                .created(URI.create("/espiritu/" + creado.getId()))
                .body(EspirituDTO.desdeModelo(creado));
    }

    @GetMapping
    public List<EspirituDTO> getAllEspiritus(){
        return espirituService.recuperarTodos().stream()
                 .map(EspirituDTO::desdeModelo).collect(Collectors.toList());
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

        Optional<Espiritu> opt = espirituService.recuperar(id);

        if (opt.isEmpty())
            return ResponseEntity.notFound().build();

        Espiritu medium = opt.get();
        dto.actualizarModelo(medium);

        Espiritu guardado = espirituService.actualizar(medium);

        return ResponseEntity.ok(EspirituDTO.desdeModelo(guardado));
    }

//Descomentar luego de tener a MediumDTO hecho
//    @GetMapping("/{idEspiritu}/conectar/{idMedium}")
//    public ResponseEntity<EspirituDTO> conectarEspiritu(@PathVariable Long idEspiritu,@PathVariable Long idMedium){
//        Medium medium = espirituService.conectar(idEspiritu,idMedium);
//        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
//    }
}
