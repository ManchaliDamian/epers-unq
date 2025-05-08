package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medium")
public class MediumControllerREST {

    private final MediumService mediumService;
    private final UbicacionService ubicacionService;
    private final EspirituService espirituService;

    public MediumControllerREST(MediumService mediumService, UbicacionService ubicacionService, EspirituService espirituService) {
        this.mediumService = mediumService;
        this.ubicacionService = ubicacionService;
        this.espirituService = espirituService;
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
        Optional<Medium> medium = mediumService.recuperar(id);
        return medium.map(m -> ResponseEntity.ok(MediumDTO.desdeModelo(m)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/espiritus")
    public ResponseEntity<List<EspirituDTO>> recuperarEspiritus(@PathVariable Long id, @RequestParam(required = false) String tipo) {
        Optional<Medium> medium = mediumService.recuperar(id);
        return medium.map(m -> m.getEspiritus().stream()
                        .filter(e -> tipo == null || e.getTipo().equalsIgnoreCase(tipo))
                        .map(EspirituDTO::desdeModelo)
                        .collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediumDTO> updateById(@PathVariable Long id, @Valid @RequestBody UpdateMediumDTO dto) {

        Optional<Medium> opt = mediumService.recuperar(id);

        if (opt.isEmpty())
            return ResponseEntity.notFound().build();

        Medium medium = opt.get();
        dto.actualizarModelo(medium);

        Medium guardado = mediumService.actualizar(medium);

        return ResponseEntity.ok(MediumDTO.desdeModelo(guardado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Optional<Medium> existente = mediumService.recuperar(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        mediumService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<MediumDTO> CreateMedium(@Valid @RequestBody CreateMediumDTO dto) {
        Ubicacion ubicacion = ubicacionService.recuperar(dto.ubicacionId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));
        Medium medium = dto.aModelo(ubicacion);
        Medium creado = mediumService.guardar(medium);
        URI location = URI.create("/medium/" + creado.getId());
        MediumDTO respuesta = MediumDTO.desdeModelo(creado);
        return ResponseEntity.created(location).body(respuesta);

    }

    @PostMapping("/{mediumEmisorId}/exorcizar/{mediumReceptorId}")
    public ResponseEntity exorcizar(@PathVariable Long mediumEmisorId, @PathVariable Long mediumReceptorId) {
        Optional<Medium> mediumEmisor = mediumService.recuperar(mediumEmisorId);
        Optional<Medium> mediumReceptor = mediumService.recuperar(mediumReceptorId);


        if (mediumEmisor.isEmpty() || mediumReceptor.isEmpty())
            return ResponseEntity.notFound().build();

        mediumService.exorcizar(mediumEmisorId, mediumReceptorId);
        return ResponseEntity.ok("Medium exorcizado con éxito");
    }

    @PostMapping("/{id}/descansar")
    public ResponseEntity descansar(@PathVariable Long id) {
        Optional<Medium> medium = mediumService.recuperar(id);

        if (medium.isEmpty()) return ResponseEntity.notFound().build();

        mediumService.descansar(id);
        return ResponseEntity.ok("Medium descansado con éxito");
    }

    @PostMapping("/{id}/invocar/{espirituId}")
    public ResponseEntity invocar(@PathVariable Long id, @PathVariable Long espirituId) {
        Optional<Medium> medium = mediumService.recuperar(id);
        Optional<Espiritu> espiritu = espirituService.recuperar(id);

        if (espiritu.isEmpty() || medium.isEmpty())
            return ResponseEntity.notFound().build();

        mediumService.invocar(id, espirituId);
        return ResponseEntity.ok("Espiritu invocado con éxito");
    }

    @PostMapping("/{id}/mover/{ubicacionId}")
    public ResponseEntity mover(@PathVariable Long id, @PathVariable Long ubicacionId) {
        Optional<Medium> medium = mediumService.recuperar(id);
        Optional<Ubicacion> ubicacion = ubicacionService.recuperar(id);

        if (ubicacion.isEmpty() || medium.isEmpty())
            return ResponseEntity.notFound().build();

        mediumService.mover(id, ubicacionId);
        return ResponseEntity.ok("Medium movido con éxito");
    }
}
