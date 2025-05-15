package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicaciones.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.MediumNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medium")
public class MediumControllerREST {

    private final MediumService mediumService;
    private final UbicacionService ubicacionService;

    public MediumControllerREST(MediumService mediumService, UbicacionService ubicacionService) {
        this.mediumService = mediumService;
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public List<MediumDTO> getAllMediums() {
        return mediumService.recuperarTodos()
                .stream()
                .map(MediumDTO::desdeModelo)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediumDTO> getMediumById(@PathVariable Long id) {
        Medium medium = mediumService.recuperar(id).orElseThrow(() -> new MediumNoEncontradoException(id));
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    @GetMapping("/{id}/espiritus")
    public List<EspirituDTO> recuperarEspiritus(@PathVariable Long id, @RequestParam(required = false) String tipo) {
        Medium medium = mediumService.recuperar(id).orElseThrow(() -> new MediumNoEncontradoException(id));
        return medium.getEspiritus().stream()
                        .filter(e -> tipo == null || e.getTipo().equalsIgnoreCase(tipo))
                        .map(EspirituDTO::desdeModelo)
                        .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediumDTO> updateById(@PathVariable Long id, @Valid @RequestBody UpdateMediumDTO dto) {

        Medium medium = mediumService.recuperar(id).orElseThrow(() -> new MediumNoEncontradoException(id));

        dto.actualizarModelo(medium);

        Medium guardado = mediumService.actualizar(medium);

        return ResponseEntity.ok(MediumDTO.desdeModelo(guardado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mediumService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<MediumDTO> createMedium(@Valid @RequestBody CreateMediumDTO dto) {
        Ubicacion ubicacion = ubicacionService.recuperar(dto.ubicacionId()).orElseThrow(() -> new UbicacionNoEncontradaException(dto.ubicacionId()));
        Medium medium = dto.aModelo(ubicacion);
        Medium creado = mediumService.guardar(medium);
        URI location = URI.create("/medium/" + creado.getId());
        MediumDTO respuesta = MediumDTO.desdeModelo(creado);
        return ResponseEntity.created(location).body(respuesta);
    }

    @PutMapping("/{mediumEmisorId}/exorcizar/{mediumReceptorId}")
    public ResponseEntity<String> exorcizar(@PathVariable Long mediumEmisorId, @PathVariable Long mediumReceptorId) {
        mediumService.exorcizar(mediumEmisorId, mediumReceptorId);
        return ResponseEntity.ok("Medium exorcizado con éxito");
    }

    @PutMapping("/{id}/descansar")
    public ResponseEntity<String> descansar(@PathVariable Long id) {
        Medium medium = mediumService.recuperar(id).orElseThrow(() -> new MediumNoEncontradoException(id));;

        mediumService.descansar(id);
        return ResponseEntity.ok("Medium descansado con éxito");
    }

    @PutMapping("/{id}/invocar/{espirituId}")
    public ResponseEntity<EspirituDTO> invocar(@PathVariable Long id, @PathVariable Long espirituId) {
        Espiritu espiritu = mediumService.invocar(id, espirituId);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(espiritu));
    }

    @PutMapping("/{id}/mover/{ubicacionId}")
    public ResponseEntity<String> mover(@PathVariable Long id, @PathVariable Long ubicacionId) {
        Medium medium = mediumService.recuperar(id).orElseThrow(() -> new MediumNoEncontradoException(id));
        Ubicacion ubicacion = ubicacionService.recuperar(ubicacionId).orElseThrow(() -> new UbicacionNoEncontradaException(id));

        mediumService.mover(id, ubicacionId);
        return ResponseEntity.ok("Espíritu movido con éxito");
    }

}
